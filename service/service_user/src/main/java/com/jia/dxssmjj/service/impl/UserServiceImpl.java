package com.jia.dxssmjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.dxssmjj.common.exception.CustomException;
import com.jia.dxssmjj.common.helper.JwtHelper;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.common.result.ResultCodeEnum;
import com.jia.dxssmjj.common.utils.AuthStatusEnum;
import com.jia.dxssmjj.makeAppointment.client.MakeAppointmentFeignClient;
import com.jia.dxssmjj.mapper.UserMapper;
import com.jia.dxssmjj.model.dto.LogonDTO;
import com.jia.dxssmjj.model.dto.UserAuthDTO;
import com.jia.dxssmjj.model.dto.UserLoginDTO;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.model.po.user.User;
import com.jia.dxssmjj.service.UserService;
import com.jia.dxssmjj.tutor.client.TutorFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MakeAppointmentFeignClient makeAppointmentFeignClient;

    @Autowired
    private TutorFeignClient tutorFeignClient;

    @Override
    public Map<String, Object> loginUser(UserLoginDTO userLoginDTO) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(userLoginDTO.getCode()==null||userLoginDTO.getCode()==""){
            queryWrapper.eq("email",userLoginDTO.getEmail());
            String password = DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes());
            queryWrapper.eq("password",password);
        }else {
            String redisCode = redisTemplate.opsForValue().get(userLoginDTO.getPhone());
            if(!redisCode.equals(userLoginDTO.getCode())) {
                throw new CustomException(ResultCodeEnum.CODE_ERROR);
            }
            queryWrapper.eq("phone",userLoginDTO.getPhone());
        }
        User user = baseMapper.selectOne(queryWrapper);
        if(user == null && StringUtils.hasText(userLoginDTO.getPhone())){
            user = new User();
            user.setPhone(userLoginDTO.getPhone());
            user.setNickName(userLoginDTO.getPhone());

            baseMapper.insert(user);
        }
        HashMap<String, Object> map = new HashMap<>();
        if(user == null && StringUtils.hasText(userLoginDTO.getEmail())){
            map.put("code",500);
            map.put("message","登录失败");
            return map;
        }
        String name = user.getNickName();
        if(!StringUtils.hasText(name)){
            name = user.getPhone();
        }

        map.put("name",name);

        // token生成
        String token = JwtHelper.createToken(user.getId(), name);
        map.put("token",token);
        map.put("role",user.getRole());

        return map;
    }

    @Override
    public void userAuth(Long userId, UserAuthDTO authDTO) {
        User user = baseMapper.selectById(userId);

        user.setRealName(authDTO.getName());
        user.setCertificatesType(authDTO.getCertificatesType());
        user.setCertificatesNo(authDTO.getCertificatesNo());
        user.setCertificatesUrl(authDTO.getCertificatesUrl());
        user.setCertificatesStatus(AuthStatusEnum.AUTH_RUN.getStatus());

        baseMapper.updateById(user);
    }

    @Override
    public Result logon(LogonDTO logonDTO) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",logonDTO.getEmail());

        User user = baseMapper.selectOne(queryWrapper);
        if(user != null){
            return Result.fail("用户已存在");
        }
        if(!logonDTO.getPassword().equals(logonDTO.getRePassword())){
            return Result.fail("两次密码不一样");
        }
        String password = DigestUtils.md5DigestAsHex(logonDTO.getPassword().getBytes());
        String nickName = logonDTO.getEmail();
        user = new User();
        user.setNickName(nickName);
        user.setEmail(logonDTO.getEmail());
        user.setPassword(password);

        baseMapper.insert(user);
        return Result.ok();

    }

    @Override
    public Result payment(Long makeAppointmentId, Long userId) {
        MakeAppointment makeAppointment = makeAppointmentFeignClient.getMakeAppointmentById(makeAppointmentId);
        User user = baseMapper.selectById(userId);
        User superUser = baseMapper.selectById(1);
        BigDecimal superUserBalance = superUser.getBalance();
        BigDecimal userBalance = user.getBalance();

        if(userBalance.compareTo(makeAppointment.getTotal()) == 1 || userBalance.compareTo(makeAppointment.getTotal()) == 0){
            BigDecimal newUserBalance = userBalance.subtract(makeAppointment.getTotal());
            user.setBalance(newUserBalance);
            superUser.setBalance(superUserBalance.add(makeAppointment.getTotal()));
            int sudo = baseMapper.updateById(superUser);
            int i = baseMapper.updateById(user);
            Result result = makeAppointmentFeignClient.updateMakeAppointmentById(makeAppointmentId, "2");
            if(sudo == 1 && i==1 && result.getCode() == 200){
                return Result.ok();
            }
            return Result.fail();
        }
        return Result.fail("余额不足");

    }

    @Override
    public Result confirm(Long makeAppointmentId) {
        MakeAppointment makeAppointment = makeAppointmentFeignClient.getMakeAppointmentById(makeAppointmentId);
        Long tutorId = makeAppointment.getTutorId();
        TutorSet tutor = tutorFeignClient.findById(tutorId);
        Long userId = tutor.getUserId();

        User user = baseMapper.selectById(userId);
        User superUser = baseMapper.selectById(1);
        BigDecimal superUserBalance = superUser.getBalance();
        BigDecimal userBalance = user.getBalance();

        user.setBalance(userBalance.add(makeAppointment.getTotal()));
        superUser.setBalance(superUserBalance.subtract(makeAppointment.getTotal()));

        int i = baseMapper.updateById(user);
        int b = baseMapper.updateById(superUser);

        Result result = makeAppointmentFeignClient.updateMakeAppointmentById(makeAppointmentId, "5");
        if(b == 1 && i==1 && result.getCode() == 200){
            return Result.ok();
        }
        return Result.fail();

    }

    @Override
    @Transactional
    public Result bindUserPhone(String phone, String code, Long userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone",phone);

        User one = baseMapper.selectOne(queryWrapper);
        if(one != null){
            return Result.fail("一个手机号只能绑定一个号码");
        }
        String codeFromRedis = redisTemplate.opsForValue().get(phone);
        if(StringUtils.hasText(codeFromRedis)){
            if(code.equals(codeFromRedis)){
                User user = baseMapper.selectById(userId);
                user.setPhone(phone);
                baseMapper.updateById(user);
                return Result.ok();
            }
            return Result.fail("验证码不一致");
        }
        return Result.fail();
    }
}

package com.jia.dxssmjj.controller;

import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.common.utils.AuthContextHolder;
import com.jia.dxssmjj.model.dto.LogonDTO;
import com.jia.dxssmjj.model.dto.UpdateUserDTO;
import com.jia.dxssmjj.model.dto.UserAuthDTO;
import com.jia.dxssmjj.model.dto.UserLoginDTO;
import com.jia.dxssmjj.model.po.user.User;
import com.jia.dxssmjj.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
@Api(value = "用户api接口",tags = "用户api接口")
@CrossOrigin
@RequestMapping("/user/api")
public class UserApiController {

    @Autowired
    private UserService userService;


    @ApiOperation("用户登录")
    @PostMapping("login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO){
        System.out.println(userLoginDTO);

        Map<String,Object> info =  userService.loginUser(userLoginDTO);

        return Result.ok(info);
    }

    @PutMapping("bindUserPhone")
    public Result bindUserPhone(@RequestParam("phone") String phone,
                                @RequestParam("code") String code,
                                HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        return userService.bindUserPhone(phone,code,userId);
    }

    @PostMapping("logon")
    @ApiOperation("用户注册")
    @Transactional
    public Result logon(@RequestBody LogonDTO logonDTO){
        Result result =  userService.logon(logonDTO);
        return result;
    }

    @PostMapping("updateUser")
    @ApiOperation("更新用户信息")
    @Transactional
    public Result updateUser(@RequestBody UpdateUserDTO updateUserDTO, HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        User user = userService.getById(userId);

        try {
            user.setNickName(updateUserDTO.getNickName());
            user.setEmail(updateUserDTO.getEmail());
            user.setHeadImg(updateUserDTO.getHeadImg());
            String password = DigestUtils.md5DigestAsHex(updateUserDTO.getPassword().getBytes());
            if(!password.equals(user.getPassword())) {
                user.setPassword(password);
            }else {
                return Result.fail("新密码不能和前密码一致");
            }
            user.setPhone(updateUserDTO.getPhone());
            user.setBalance(updateUserDTO.getBalance());
            userService.updateById(user);
            return Result.ok();
        }catch (DuplicateKeyException ex){
            if(ex.getMessage().contains("Duplicate entry")){
                String msg = "已存在";
                return Result.fail(msg);
            }
            return Result.fail("未知错误了");
        }
    }

    @PostMapping("auth/userAuth")
    @Transactional
    public Result userAuth(@RequestBody UserAuthDTO authDTO, HttpServletRequest request){
        userService.userAuth(AuthContextHolder.getUserId(request),authDTO);
        return Result.ok();
    }

    @GetMapping("auth/getUser")
    public Result getUser(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        User user = userService.getById(userId);
        return Result.ok(user);
    }

    @GetMapping("getUser")
    public User getUser(Long userId){
        User user = userService.getById(userId);
        return user;
    }

    @GetMapping("payment")
    public Result payment(@RequestParam("makeAppointmentId") Long makeAppointmentId, HttpServletRequest request){

        Result result = userService.payment(makeAppointmentId, AuthContextHolder.getUserId(request));
        return  result;
    }

    @PutMapping("confirmMakeAppointment")
    @Transactional
    public Result confirmMakeAppointment(@RequestParam("makeAppointmentId") Long makeAppointmentId){
        Result result = userService.confirm(makeAppointmentId);
        return result;
    }

    @PutMapping("updateRole")
    public Integer updateRole(@RequestParam("userId") Long userId){
        User user = userService.getById(userId);
        user.setRole("1");

        boolean b = userService.updateById(user);
        if(b){
            return 1;
        }
        return 0;
    }

}

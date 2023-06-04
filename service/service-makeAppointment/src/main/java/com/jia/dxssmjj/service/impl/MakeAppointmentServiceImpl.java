package com.jia.dxssmjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.dxssmjj.common.utils.AuthContextHolder;
import com.jia.dxssmjj.mapper.MakeAppointmentMapper;
import com.jia.dxssmjj.model.dto.MyAppointmentDTO;
import com.jia.dxssmjj.model.dto.TutorAcceptDTO;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.model.po.user.User;
import com.jia.dxssmjj.service.MakeAppointmentService;
import com.jia.dxssmjj.tutor.client.TutorFeignClient;
import com.jia.dxssmjj.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class MakeAppointmentServiceImpl extends ServiceImpl<MakeAppointmentMapper, MakeAppointment> implements MakeAppointmentService {

    @Autowired
    private MakeAppointmentMapper makeAppointmentMapper;
    @Resource
    private TutorFeignClient tutorFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public List<MyAppointmentDTO> findMakeAppointmentByStatus(MakeAppointment makeAppointment, HttpServletRequest request) {

        QueryWrapper<MakeAppointment> queryWrapper = new QueryWrapper<>();
        if(makeAppointment.getStatus() == ""){
            queryWrapper.like("status",makeAppointment.getStatus());
        }else {
            queryWrapper.eq("status",makeAppointment.getStatus());
        }
        Long userId = AuthContextHolder.getUserId(request);

        queryWrapper.eq("user_id",userId);
        queryWrapper.gt("tutor_id",0).eq("auth",1);


        List<MakeAppointment> makeAppointmentList = baseMapper.selectList(queryWrapper);

        List<MyAppointmentDTO> list = new ArrayList<>();

        for (MakeAppointment appointment : makeAppointmentList){
            MyAppointmentDTO myAppointmentDTO = new MyAppointmentDTO();
            TutorSet tutorSet = tutorFeignClient.findById(appointment.getTutorId());
            myAppointmentDTO.setHeadImageUrl(tutorSet.getHeadImageUrl());
            myAppointmentDTO.setPersonIntr(tutorSet.getPersonIntr());
            myAppointmentDTO.setTutorName(tutorSet.getNickname());
            myAppointmentDTO.setMakeAppointmentId(appointment.getId());
            myAppointmentDTO.setTutorId(appointment.getTutorId());
            myAppointmentDTO.setStatus(appointment.getStatus());
            myAppointmentDTO.setTotal(appointment.getTotal());
            myAppointmentDTO.setSubject(appointment.getSubject());
            myAppointmentDTO.setHourString(appointment.getHourString());
            list.add(myAppointmentDTO);
        }

        return list;
    }



    @Override
    public List<TutorAcceptDTO> findMakeAppointmentByStatusFromTutor(MakeAppointment makeAppointment,
                                                                       HttpServletRequest request) {
        QueryWrapper<MakeAppointment> queryWrapper = new QueryWrapper<>();
        if(makeAppointment.getStatus().equals("7")){
            queryWrapper.in("status","5","7");
        }
        if(makeAppointment.getStatus().equals("1")){
            queryWrapper.in("status","1");
        }else{
            queryWrapper.eq("status",makeAppointment.getStatus());
        }
        Long userId = AuthContextHolder.getUserId(request);
        TutorSet tutor = tutorFeignClient.findTutorIdByUserId(userId);
        Long tutorId = tutor.getId();

        queryWrapper.eq("tutor_id",tutorId);
        List<MakeAppointment> makeAppointmentList = baseMapper.selectList(queryWrapper);

        List<TutorAcceptDTO> list = new ArrayList<>();
        for(MakeAppointment m: makeAppointmentList){
            User user = userFeignClient.getUser(m.getUserId());
            TutorAcceptDTO tutorAcceptDTO = new TutorAcceptDTO();
            tutorAcceptDTO.setMakeAppointmentId(m.getId());
            tutorAcceptDTO.setUserHeadImgUrl(user.getHeadImg());
            tutorAcceptDTO.setTotal(m.getTotal());
            tutorAcceptDTO.setHourString(m.getHourString());
            tutorAcceptDTO.setSubject(m.getSubject());
            tutorAcceptDTO.setAddress(m.getAddress());
            tutorAcceptDTO.setRealName(m.getRealName());
            tutorAcceptDTO.setPhone(m.getPhone());
            tutorAcceptDTO.setWeChat(m.getWeChat());
            tutorAcceptDTO.setRemark(m.getRemark());
            list.add(tutorAcceptDTO);
        }


        return list;
    }

    @Override
    public List<MakeAppointment> findComplete(Long tutorId) {
        return makeAppointmentMapper.findComplete(tutorId);
    }

    @Override
    public boolean deleteMakeAppointment(MyAppointmentDTO myAppointmentDTO) {
        int i = baseMapper.deleteById(myAppointmentDTO.getMakeAppointmentId());
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean cancelMakeAppointment(MyAppointmentDTO myAppointmentDTO) {

        MakeAppointment makeAppointment = baseMapper.selectById(myAppointmentDTO.getMakeAppointmentId());
        makeAppointment.setStatus(myAppointmentDTO.getStatus());

        int i = baseMapper.updateById(makeAppointment);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public boolean AgainMakeAppointmentById(MyAppointmentDTO myAppointmentDTO) {
        MakeAppointment makeAppointment = baseMapper.selectById(myAppointmentDTO.getMakeAppointmentId());
        makeAppointment.setStatus(myAppointmentDTO.getStatus());

        int i = baseMapper.updateById(makeAppointment);
        if(i == 1){
            return true;
        }
        return false;
    }

    @Override
    public List<String> getHourStringList(Long userId) {
        return makeAppointmentMapper.getHourStringList(userId);
    }
}

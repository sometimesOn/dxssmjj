package com.jia.dxssmjj.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.makeAppointment.client.MakeAppointmentFeignClient;
import com.jia.dxssmjj.mapper.TutorSetMapper;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.model.po.user.User;
import com.jia.dxssmjj.service.TutorSetService;
import com.jia.dxssmjj.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TutorSetServiceImpl extends ServiceImpl<TutorSetMapper, TutorSet> implements TutorSetService {


    @Autowired
    private MakeAppointmentFeignClient makeAppointmentFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public TutorSet findById(Long tutorId) {
        TutorSet tutorSet = baseMapper.selectById(tutorId);

        return tutorSet;
    }

    @Override
    public void tutorAuth(Long userId, TutorSet tutorSet) {

        User user = userFeignClient.getUser(userId);

        tutorSet.setHeadImageUrl(user.getHeadImg());
        tutorSet.setNickname(user.getNickName());
        tutorSet.setCollegeAuth(1);
        tutorSet.setUserId(userId);
        userFeignClient.updateRole(userId);

        baseMapper.insert(tutorSet);

    }

    @Override
    public Result collectionMoney(Long makeAppointmentId, Long userId) {

        Result result = makeAppointmentFeignClient.updateMakeAppointmentById(makeAppointmentId, "4");

        return result;

    }
}

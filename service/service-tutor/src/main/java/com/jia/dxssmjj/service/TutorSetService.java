package com.jia.dxssmjj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.po.tutor.TutorSet;

import java.util.List;

public interface TutorSetService extends IService<TutorSet> {

    TutorSet findById(Long tutorId);

    void tutorAuth(Long userId, TutorSet tutorSet);

    Result collectionMoney(Long makeAppointmentId, Long userId);
}

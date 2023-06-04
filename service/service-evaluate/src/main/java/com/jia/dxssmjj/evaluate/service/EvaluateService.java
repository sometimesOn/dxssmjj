package com.jia.dxssmjj.evaluate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.po.evaluate.Evaluate;

public interface EvaluateService extends IService<Evaluate> {
    Result getEvaluateAverageRate(Long tutorId);
}

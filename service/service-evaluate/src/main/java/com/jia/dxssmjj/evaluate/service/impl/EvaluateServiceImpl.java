package com.jia.dxssmjj.evaluate.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.dxssmjj.evaluate.mapper.EvaluateMapper;
import com.jia.dxssmjj.evaluate.service.EvaluateService;
import com.jia.dxssmjj.model.po.evaluate.Evaluate;
import org.springframework.stereotype.Service;

@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements EvaluateService {
}

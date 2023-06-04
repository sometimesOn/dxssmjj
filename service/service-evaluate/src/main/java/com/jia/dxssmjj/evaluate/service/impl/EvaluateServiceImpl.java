package com.jia.dxssmjj.evaluate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.evaluate.mapper.EvaluateMapper;
import com.jia.dxssmjj.evaluate.service.EvaluateService;
import com.jia.dxssmjj.model.dto.EvaluateAndUserDTO;
import com.jia.dxssmjj.model.po.evaluate.Evaluate;
import com.jia.dxssmjj.model.po.user.User;
import com.jia.dxssmjj.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements EvaluateService {


    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public Result getEvaluateAverageRate(Long tutorId) {

        QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tutor_id",tutorId).orderByDesc("create_time").last("limit 0,10");

        List<Evaluate> evaluateList = baseMapper.selectList(queryWrapper);
        List<EvaluateAndUserDTO> list = new ArrayList<>();

        for (Evaluate evaluate : evaluateList){
            EvaluateAndUserDTO dto = new EvaluateAndUserDTO();
            User user = userFeignClient.getUser(evaluate.getUserId());
            dto.setUserName(user.getNickName());
            dto.setContent(evaluate.getContent());
            dto.setRate(evaluate.getRate());
            list.add(dto);
        }
        return Result.ok(list);
    }
}

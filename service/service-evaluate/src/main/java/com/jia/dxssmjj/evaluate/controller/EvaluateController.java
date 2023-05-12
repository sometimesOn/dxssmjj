package com.jia.dxssmjj.evaluate.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.common.utils.AuthContextHolder;
import com.jia.dxssmjj.common.utils.BigDecimalUtil;
import com.jia.dxssmjj.evaluate.service.EvaluateService;
import com.jia.dxssmjj.makeAppointment.client.MakeAppointmentFeignClient;
import com.jia.dxssmjj.model.dto.EvaluateAndMakeAppointmentDTO;
import com.jia.dxssmjj.model.dto.EvaluateDTO;
import com.jia.dxssmjj.model.po.evaluate.Evaluate;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.tutor.client.TutorFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@Api(value = "评价接口",tags = "评价接口")
@RequestMapping("/evaluate/api")
public class EvaluateController {

    @Autowired
    private EvaluateService evaluateService;

    @Autowired
    private MakeAppointmentFeignClient makeAppointmentFeignClient;

    @Autowired
    private TutorFeignClient tutorFeignClient;


    @PostMapping("saveEvaluate")
    @ApiOperation("保存评价")
    @Transactional
    public Result saveEvaluate(@RequestBody EvaluateDTO evaluateDTO, HttpServletRequest request){
        if(evaluateDTO.getContent() == ""){
            return Result.fail("评价内容不能为空");
        }
        if(evaluateDTO.getRate().intValue() == 0){
            return Result.fail("评星不能为空");
        }
        Long userId = AuthContextHolder.getUserId(request);
        Evaluate evaluate = new Evaluate();
        evaluate.setUserId(userId);
        evaluate.setRate(evaluateDTO.getRate());
        evaluate.setContent(evaluateDTO.getContent());
        evaluate.setTutorId(evaluateDTO.getTutorId());
        evaluate.setMakeAppointmentId(evaluateDTO.getMakeAppointmentId());

        boolean isSave = evaluateService.save(evaluate);
        if(isSave){
            makeAppointmentFeignClient.complete(evaluateDTO.getMakeAppointmentId(),
                    evaluateDTO.getStatus());
            //获取平均评分
            QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("tutor_id",evaluateDTO.getTutorId());

            List<Evaluate> evaluateList = evaluateService.list(queryWrapper);
            BigDecimal totalRate = new BigDecimal(0);
            for (Evaluate evaluate1 : evaluateList){
                totalRate = totalRate.add(evaluate1.getRate());
            }
            BigDecimal averageRate = totalRate.divide(new BigDecimal(evaluateList.size()));

            // 更新价格
            Integer complete = makeAppointmentFeignClient.findComplete(evaluate.getTutorId());
            BigDecimal newHourPrice = new BigDecimal(complete).multiply(averageRate).
                    divide(new BigDecimal(4000)).multiply(new BigDecimal(500)).add(new BigDecimal(40));
            Integer i = tutorFeignClient.updateHourPrice(evaluateDTO.getTutorId(), newHourPrice);
            if(i == 1){
                return Result.ok();
            }
            return Result.fail();
        }
        return Result.fail();
    }


    @GetMapping("getEvaluateAndMakeAppointment")
    public Result getEvaluate(HttpServletRequest request){

        Long userId = AuthContextHolder.getUserId(request);
        TutorSet tutor = tutorFeignClient.findTutorIdByUserId(userId);

        QueryWrapper<Evaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tutor_id",tutor.getId());


        List<Evaluate> evaluateList = evaluateService.list(queryWrapper);

        List<EvaluateAndMakeAppointmentDTO> list = new ArrayList<>();

        for (Evaluate evaluate : evaluateList){
            MakeAppointment makeAppointment =  makeAppointmentFeignClient.
                    getMakeAppointmentById(evaluate.getMakeAppointmentId());
            EvaluateAndMakeAppointmentDTO dto = new EvaluateAndMakeAppointmentDTO();
            dto.setSerialNumber(makeAppointment.getSerialNumber());
            dto.setRealName(makeAppointment.getRealName());
            dto.setTotal(makeAppointment.getTotal());
            dto.setSubject(makeAppointment.getSubject());
            dto.setHourString(makeAppointment.getHourString());
            dto.setAddress(makeAppointment.getAddress());
            dto.setRemark(makeAppointment.getRemark());
            dto.setContent(evaluate.getContent());
            dto.setRate(evaluate.getRate());
            list.add(dto);
        }

        return Result.ok(list);
    }


}

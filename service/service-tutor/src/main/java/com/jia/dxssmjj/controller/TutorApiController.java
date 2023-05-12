package com.jia.dxssmjj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.common.utils.AuthContextHolder;
import com.jia.dxssmjj.model.dto.QuerySingleDTO;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.service.TutorSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@Api(value = "家教信息管理接口",tags = "家教信息管理接口")
@RequestMapping("/tutor/api")
public class TutorApiController {

    @Autowired
    private TutorSetService tutorSetService;


    @GetMapping("findSubjectList")
    @ApiOperation(value = "找出家教的教学科目")
    public Result findSubjectList(){

        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT good_subject");
        queryWrapper.eq("college_auth",2);

        List<TutorSet> list = tutorSetService.list(queryWrapper);
        return Result.ok(list);
    }

    @PostMapping("findByNickname")
    @ApiOperation(value = "自动补全")
    public Result findByNickname(@RequestBody QuerySingleDTO querySingleDTO){

        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("nickname",querySingleDTO.getQueryString()).or().
                like("good_subject",querySingleDTO.getQueryString());
        queryWrapper.eq("college_auth",2);

        List<TutorSet> list = tutorSetService.list(queryWrapper);
        return  Result.ok(list);
    }

    @GetMapping("auth/getTutor")
    public Result getUser(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);

        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        TutorSet tutorSet = tutorSetService.getOne(queryWrapper);
        return Result.ok(tutorSet);
    }

    @PostMapping("findById")
    @ApiOperation(value = "找家教")
    public TutorSet findById(@RequestParam("tutorId") Long tutorId){

        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("college_auth",2);
        queryWrapper.eq("id",tutorId);
        TutorSet tutorSet =  tutorSetService.getOne(queryWrapper);

        return  tutorSet;
    }

    @PostMapping("findTutorIdByUserId")
    public TutorSet findTutorIdByUserId(@RequestParam("userId") Long userId){

        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        TutorSet tutorSet =  tutorSetService.getOne(queryWrapper);

        return  tutorSet;
    }

    @PostMapping("auth/tutorAuth")
    @Transactional
    public Result tutorAuth(@RequestBody TutorSet tutorSet, HttpServletRequest request){
        tutorSetService.tutorAuth(AuthContextHolder.getUserId(request),tutorSet);
        return Result.ok();
    }

    @PutMapping("collectionMoney")
    @Transactional
    public Result collectionMoney(@RequestParam("makeAppointmentId") Long makeAppointmentId,
                                  HttpServletRequest request){

        Result result = tutorSetService.collectionMoney(makeAppointmentId,AuthContextHolder.getUserId(request));

        return result;
    }

    @PutMapping("updateHourPrice")
    @Transactional
    public Integer updateHourPrice(@RequestParam("tutorId") Long tutorId,@RequestParam("hourPrice") BigDecimal hourPrice){
        TutorSet tutor = tutorSetService.getById(tutorId);

        tutor.setHourPrice(hourPrice);

        boolean b = tutorSetService.updateById(tutor);
        if(b){
            return 1;
        }else {
            return 0;
        }
    }

    @GetMapping("getHourPrice")
    public Result getHourPrice(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);

        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);

        TutorSet tutor = tutorSetService.getOne(queryWrapper);
        return Result.ok(tutor.getHourPrice());
    }
}

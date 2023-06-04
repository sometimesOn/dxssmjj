package com.jia.dxssmjj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.dto.QueryTutorParamsDTO;
import com.jia.dxssmjj.model.dto.ResultForTutorAuthDTO;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.model.po.user.User;
import com.jia.dxssmjj.service.TutorSetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@Api(value = "家教信息管理接口",tags = "家教信息管理接口")
@RequestMapping("/tutor/admin")
public class TutorSetController {

    @Autowired
    private TutorSetService tutorSetService;

    /**
     * 家教信息分页查询
     * @param queryTutorParamsDTO
     * @return
     */
    @PostMapping("findPageTutor")
    @ApiOperation(value = "家教信息分页查询")
    public Result findPageTutor(@RequestBody(required = false) QueryTutorParamsDTO queryTutorParamsDTO){

        //创建page对象，传递当前页,每页记录数
        Page<TutorSet> page = new Page<>(queryTutorParamsDTO.getCurrent(), queryTutorParamsDTO.getLimit());

        //构建条件
        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();

        String nickname = queryTutorParamsDTO.getNickname();
        String goodSubject = queryTutorParamsDTO.getGoodSubject();
        //String state = queryTutorParamsDTO.getState();

        queryWrapper.like(StringUtils.hasText(nickname),"real_name",nickname);
        queryWrapper.like(StringUtils.hasText(goodSubject),"good_subject",goodSubject);
        queryWrapper.eq("college_auth",2);

        Page<TutorSet> tutorSetPage = tutorSetService.page(page,queryWrapper);

        return Result.ok(tutorSetPage);
    }


    /**
     * 找出所有家教的信息
     * @return
     */
    @GetMapping("findAllTutor")
    @ApiOperation(value = "找出所有家教的信息")
    public Result findAllTutor(){

        List<TutorSet> list = tutorSetService.list();

        return Result.ok(list);
    }

    @PostMapping("resultForTutorAuth")
    public Result resultForTutorAuth(@RequestBody ResultForTutorAuthDTO resultForTutorAuthDTO){

        TutorSet tutor = tutorSetService.getById(resultForTutorAuthDTO.getId());

        if(resultForTutorAuthDTO.getCollegeAuth() == 2){
            tutor.setReason("");
            tutor.setCollegeAuth(resultForTutorAuthDTO.getCollegeAuth());
        }
        if(resultForTutorAuthDTO.getCollegeAuth() == -1){
            tutor.setCollegeAuth(resultForTutorAuthDTO.getCollegeAuth());
            tutor.setReason(resultForTutorAuthDTO.getReason());
        }

        boolean isUpdate = tutorSetService.updateById(tutor);

        if(isUpdate){
            return Result.ok();
        }
        return Result.fail();


    }


    @GetMapping("getTutorNeedAuth")
    @ApiOperation(value = "找出所有用户需要验证的信息")
    public Result getTutorNeedAuth(){

        QueryWrapper<TutorSet> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("college_auth",1);

        List<TutorSet> list = tutorSetService.list(queryWrapper);

        return Result.ok(list);
    }



    /**
     * 根据id获取家教信息
     * @param id
     * @return
     */
    @GetMapping("findTutorById")
    @ApiOperation(value = "根据id获取家教信息")
    public Result findTutorById(@PathVariable Long id){

        TutorSet tutorSet = tutorSetService.getById(id);

        return Result.ok(tutorSet);

    }

    /**
     * 修改家教信息
     * @param tutorSet
     * @return
     */
    @PostMapping("updateTutor")
    @ApiOperation(value = "修改家教信息")
    @Transactional
    public Result updateTutor(@RequestBody TutorSet tutorSet){

        boolean isUpdate = tutorSetService.updateById(tutorSet);
        if (isUpdate){
            return Result.ok();
        }else {
            return Result.fail();
        }

    }


    /**
     * 逻辑删除家教
     * @param id
     * @return
     */
    @DeleteMapping("deleteTutor/{id}")
    @ApiOperation("逻辑删除家教")
    public Result deleteTutor(@PathVariable Long id){

        boolean isDeleted = tutorSetService.removeById(id);
        if (isDeleted){
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }


    @DeleteMapping("deleteTutors")
    @ApiOperation("批量删除家教")
    public Result deleteTutors(@RequestBody List<TutorSet> tutorSetList){

        List<Long> ids = new ArrayList<>();
        for(TutorSet tutorSet: tutorSetList) {
            ids.add(tutorSet.getId());
        }
        boolean isDeleted = tutorSetService.removeBatchByIds(ids);
        if (isDeleted){
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }

    /**
     * 添加家教信息
     * @param tutorSet
     * @return
     */
    @PostMapping("saveTutor")
    @ApiOperation("添加家教信息")
    @Transactional
    public Result saveTutor(@RequestBody TutorSet tutorSet){

        boolean isSaved = tutorSetService.save(tutorSet);
        if (isSaved){
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }
}

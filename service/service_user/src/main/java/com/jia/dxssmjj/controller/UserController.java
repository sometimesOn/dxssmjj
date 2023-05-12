package com.jia.dxssmjj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.dto.QueryTutorParamsDTO;
import com.jia.dxssmjj.model.dto.QueryUserDTO;
import com.jia.dxssmjj.model.dto.ResultForTutorAuthDTO;
import com.jia.dxssmjj.model.dto.ResultForUserAuthDTO;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.model.po.user.User;
import com.jia.dxssmjj.service.UserService;
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
@Slf4j
@Api(value = "用户管理接口",tags = "用户管理接口")
@CrossOrigin
@RequestMapping("/user/admin")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("findPageUser")
    @ApiOperation(value = "信息分页查询")
    public Result findPageTutor(@RequestBody(required = false) QueryUserDTO queryUserDTO){

        //创建page对象，传递当前页,每页记录数
        Page<User> page = new Page<>(queryUserDTO.getCurrent(), queryUserDTO.getLimit());

        //构建条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        String name = queryUserDTO.getName();

        queryWrapper.like(StringUtils.hasText(name),"nick_name",name).or().like("real_name",name);

        Page<User> tutorSetPage = userService.page(page,queryWrapper);

        return Result.ok(tutorSetPage);
    }

    /**
     * 找出所有家教的信息
     * @return
     */
    @GetMapping("findAllUser")
    @ApiOperation(value = "找出所有用户的信息")
    public Result findAllTutor(){

        List<User> list = userService.list();

        return Result.ok(list);
    }


    @GetMapping("getUserNeedAuth")
    @ApiOperation(value = "找出所有用户需要验证的信息")
    public Result getUserNeedAuth(){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("certificates_status",1);
        queryWrapper.eq("role",'0');

        List<User> list = userService.list(queryWrapper);

        return Result.ok(list);
    }

    @PostMapping("resultForUserAuth")
    public Result resultForUserAuth(@RequestBody ResultForUserAuthDTO resultForUserAuthDTO){

        User user = userService.getById(resultForUserAuthDTO.getId());

        user.setCertificatesStatus(resultForUserAuthDTO.getCertificatesStatus());
        if(resultForUserAuthDTO.getCertificatesStatus() == 2){
            user.setReason("");
        }
        if(resultForUserAuthDTO.getCertificatesStatus() == -1){
            user.setReason(resultForUserAuthDTO.getReason());
        }

        boolean isUpdate = userService.updateById(user);

        if(isUpdate){
            return Result.ok();
        }
        return Result.fail();


    }



    /**
     * 根据id获取家教信息
     * @param id
     * @return
     */
    @GetMapping("findUserById")
    @ApiOperation(value = "根据id获取家教信息")
    public Result findTutorById(@PathVariable Long id){

        User user  = userService.getById(id);

        return Result.ok(user);

    }

    /**
     * 修改家教信息
     * @return
     */
    @PostMapping("updateUser")
    @ApiOperation(value = "修改家教信息")
    public Result updateTutor(@RequestBody User user){

        boolean isUpdate = userService.updateById(user);
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
    @DeleteMapping("deleteUser/{id}")
    @ApiOperation("逻辑删除家教")
    public Result deleteTutor(@PathVariable Long id){

        boolean isDeleted = userService.removeById(id);
        if (isDeleted){
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }


    @DeleteMapping("deleteUsers")
    @ApiOperation("批量删除家教")
    @Transactional
    public Result deleteTutors(@RequestBody List<User> userList){

        List<Long> ids = new ArrayList<>();
        for(User user: userList) {
            ids.add(user.getId());
        }
        boolean isDeleted = userService.removeBatchByIds(ids);
        if (isDeleted){
            return Result.ok();
        }
        else {
            return Result.fail();
        }
    }



}

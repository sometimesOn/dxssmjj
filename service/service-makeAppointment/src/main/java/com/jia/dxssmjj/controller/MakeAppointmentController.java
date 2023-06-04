package com.jia.dxssmjj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import com.jia.dxssmjj.service.MakeAppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
@Api(value = "家教预约管理接口",tags = "家教预约管理接口")
@RequestMapping("/makeAppointment/admin")
public class MakeAppointmentController {

    @Autowired
    private MakeAppointmentService makeAppointmentService;

    @GetMapping("findNoTutor")
    @ApiOperation("找寻无主的订单")
    public Result findNoTutor(){
        QueryWrapper<MakeAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tutor_id",0).orderByDesc("create_time").eq("auth",0);

        List<MakeAppointment> list = makeAppointmentService.list(queryWrapper);
        return Result.ok(list);
    }

    @PutMapping("resultForMakeAppointmentAuth")
    @ApiOperation("审核订单")
    public Result resultForMakeAppointmentAuth(@RequestParam("id") Long id,
                                               @RequestParam("auth") Integer auth){
        QueryWrapper<MakeAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",id);

        MakeAppointment makeAppointment = makeAppointmentService.getOne(queryWrapper);
        makeAppointment.setAuth(auth);

        boolean b = makeAppointmentService.updateById(makeAppointment);
        if(b){
            return Result.ok();
        }
        return Result.fail();
    }
}

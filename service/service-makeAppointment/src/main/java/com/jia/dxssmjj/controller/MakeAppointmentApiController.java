package com.jia.dxssmjj.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.common.utils.AuthContextHolder;
import com.jia.dxssmjj.model.dto.MakeAppointmentDTO;
import com.jia.dxssmjj.model.dto.MyAppointmentDTO;
import com.jia.dxssmjj.model.dto.TutorAcceptDTO;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import com.jia.dxssmjj.model.po.tutor.TutorSet;
import com.jia.dxssmjj.service.MakeAppointmentService;
import com.jia.dxssmjj.tutor.client.TutorFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@Slf4j
@Api(value = "家教预约接口",tags = "家教预约接口")
@RequestMapping("/makeAppointment/api")
public class MakeAppointmentApiController {

    @Autowired
    private MakeAppointmentService makeAppointmentService;

    @Autowired
    private TutorFeignClient tutorFeignClient;


    @PostMapping("acceptMakeAppointment")
    @ApiOperation("接受订单")
    public Result acceptMakeAppointment(
            @RequestBody MakeAppointment makeAppointment,
            HttpServletRequest request){

        Long userId = AuthContextHolder.getUserId(request);
        TutorSet tutor = tutorFeignClient.findTutorIdByUserId(userId);

        MakeAppointment appointment =
                makeAppointmentService.getById(makeAppointment.getId());
        appointment.setStatus("3");
        appointment.setTutorId(tutor.getId());
        boolean update = makeAppointmentService.updateById(appointment);

        if(update){
            return Result.ok();
        }
        return Result.fail();
    }

    @GetMapping("findMakeAppointmentNoTutor")
    @ApiOperation("找寻无主的订单")
    public Result findMakeAppointmentNoTutor(
            @RequestParam("queryString") String queryString,
            @RequestParam("limit") Long limit,
            @RequestParam("current") Long current){
        // 创建Page对象
        Page<MakeAppointment> page = new Page<>(current,limit);
        // 创建条件构造器
        QueryWrapper<MakeAppointment> queryWrapper = new QueryWrapper<>();
        // 条件约束
        queryWrapper.eq("tutor_id",0).
                orderByDesc("create_time")
                .like("subject",queryString)
                .eq("auth",1);

        //查找数据库中的数据并返回Page对象
        Page<MakeAppointment> makeAppointmentPage =
                makeAppointmentService.page(page,queryWrapper);
        return Result.ok(makeAppointmentPage);
    }

    @ApiOperation(value = "添加预约记录")
    @PostMapping("saveMakeAppointment")
    @Transactional
    public Result saveMakeAppointment(
            @RequestBody MakeAppointmentDTO makeAppointmentDTO,
            HttpServletRequest request){

        String hourString = "";

        for(List<String> stringList : makeAppointmentDTO.getHour()){
            for(String s : stringList){
                hourString = hourString + s;
            }
        }

        // 获取请求用户的id
        Long userId = AuthContextHolder.getUserId(request);

        //判断时间是否重复
        List<String> hourStringList =
                makeAppointmentService.getHourStringList(userId);
        String hourStringOld = "";
        for(String s : hourStringList){
            hourStringOld = hourStringOld + s;
        }
        for(int i = 0; i < hourString.length()/5; i++ ){
            String c = hourString.substring(i*5,5*(i+1));
            for(int j = 0; j < hourStringOld.length()/5; j++){
                String b = hourStringOld.substring(j*5,5*(j+1));
                if(b.equals(c)){
                    return Result.fail(c + "时间段已有课程，不可重复选");
                }
            }
        }

        // 判断是需求订单还是预约订单
        MakeAppointment makeAppointment = new MakeAppointment();
        if(makeAppointmentDTO.getTutorId() > 0){
            TutorSet tutorSet =
                    tutorFeignClient.findById(makeAppointmentDTO.getTutorId());
            BigDecimal total =
                    new BigDecimal(makeAppointmentDTO.getHour().size()*2)
                            .multiply(tutorSet.getHourPrice());
            makeAppointment.setTotal(total);
        } else if (makeAppointmentDTO.getTutorId() == 0) {
            BigDecimal total = makeAppointmentDTO.getPrice();
            makeAppointment.setTotal(total);
        }

        // 对象赋值
        String serialNumber  = UUID.randomUUID().toString()
                + System.currentTimeMillis();
        makeAppointment.setHourString(hourString);
        makeAppointment.setUserId(userId);
        makeAppointment.setTutorId(makeAppointmentDTO.getTutorId());
        makeAppointment.setRealName(makeAppointmentDTO.getRealName());
        makeAppointment.setPhone(makeAppointmentDTO.getPhone());
        makeAppointment.setWeChat(makeAppointmentDTO.getWeChat());
        makeAppointment.setGender(makeAppointmentDTO.getGender());
        makeAppointment.setSubject(makeAppointmentDTO.getSubject());
        makeAppointment.setAddress(makeAppointmentDTO.getAddress());
        makeAppointment.setRemark(makeAppointmentDTO.getRemark());
        makeAppointment.setSerialNumber(serialNumber);

        boolean isSave = makeAppointmentService.save(makeAppointment);

        if(isSave){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("查询预约表")
    @PostMapping("findMakeAppointmentByStatus")
    public Result findMakeAppointmentByStatus(
            @RequestBody MakeAppointment makeAppointment,
            HttpServletRequest request){

        // 查询预约表
        List<MyAppointmentDTO> list =
                makeAppointmentService.findMakeAppointmentByStatus
                        (makeAppointment, request);

        return Result.ok(list);
    }


    @PostMapping("findMakeAppointmentByStatusFromTutor")
    @ApiOperation("教员端查找订单列表")
    public Result findMakeAppointmentByStatusFromTutor(
            @RequestBody MakeAppointment makeAppointment,
            HttpServletRequest request){

        List<TutorAcceptDTO> list =
                makeAppointmentService.findMakeAppointmentByStatusFromTutor
                        (makeAppointment, request);

        return Result.ok(list);
    }

    @ApiOperation("取消预约记录")
    @PostMapping("cancelMakeAppointment")
    @Transactional
    public Result cancelMakeAppointment(
            @RequestBody MyAppointmentDTO myAppointmentDTO){
        boolean isCancel =
                makeAppointmentService.cancelMakeAppointment(myAppointmentDTO);
        if(isCancel){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("删除预约记录")
    @PostMapping("deleteMakeAppointment")
    @Transactional
    public Result deleteMakeAppointment(
            @RequestBody MyAppointmentDTO myAppointmentDTO){
        boolean isDelete = makeAppointmentService.
                deleteMakeAppointment(myAppointmentDTO);
        if(isDelete){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("重新预约")
    @PostMapping("AgainMakeAppointmentById")
    public Result AgainMakeAppointmentById(
            @RequestBody MyAppointmentDTO myAppointmentDTO){
        boolean isCancel = makeAppointmentService.
                AgainMakeAppointmentById(myAppointmentDTO);
        if(isCancel){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @PostMapping("complete")
    @ApiOperation("完成订单")
    public String complete(Long makeAppointmentId,String status){

        MakeAppointment makeAppointment =
                makeAppointmentService.getById(makeAppointmentId);
        makeAppointment.setStatus(status);

        makeAppointmentService.updateById(makeAppointment);
        return null;

    }

    @GetMapping("getMakeAppointmentById")
    @ApiOperation("根据id查找订单")
    public MakeAppointment getMakeAppointmentById(
            @RequestParam("makeAppointmentId") Long makeAppointmentId){
        MakeAppointment makeAppointment =
                makeAppointmentService.getById(makeAppointmentId);
        return makeAppointment;
    }

    @GetMapping("getMakeAppointmentByIdFromTutor")
    @ApiOperation("教员端根据id查找订单")
    public MakeAppointment getMakeAppointmentByIdFromTutor(
            @RequestParam("userId") Long userId){
        TutorSet tutor = tutorFeignClient.findTutorIdByUserId(userId);
        QueryWrapper<MakeAppointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tutor_id",tutor.getId());

        MakeAppointment one = makeAppointmentService.getOne(queryWrapper);

        MakeAppointment makeAppointment =
                makeAppointmentService.getById(one.getId());
        return makeAppointment;
    }

    @GetMapping("updateMakeAppointmentById")
    @ApiOperation("更新订单")
    public Result updateMakeAppointmentById(
            @RequestParam("makeAppointmentId") Long makeAppointmentId,
            @RequestParam("status") String status){
        MakeAppointment makeAppointment =
                makeAppointmentService.getById(makeAppointmentId);
        makeAppointment.setStatus(status);

        boolean b = makeAppointmentService.updateById(makeAppointment);
        if(b){
            return Result.ok();
        }
        return Result.fail();

    }


    @GetMapping("findComplete")
    @ApiOperation("查找完成的订单")
    public Integer findComplete(
            @RequestParam("tutorId") Long tutorId){

        List<MakeAppointment> list =
                makeAppointmentService.findComplete(tutorId);
        return list.size();
    }

}

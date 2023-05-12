package com.jia.dxssmjj.makeAppointment.client;

import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.po.makeAppointment.MakeAppointment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient("service-makeAppointment")
@Component
public interface MakeAppointmentFeignClient {

    @PostMapping("/makeAppointment/api/complete")
    public String complete(@RequestParam("makeAppointmentId") Long makeAppointmentId,
                            @RequestParam("status") String status);

    @GetMapping("/makeAppointment/api/getMakeAppointmentById")
    public MakeAppointment getMakeAppointmentById(@RequestParam("makeAppointmentId") Long makeAppointmentId);

    @GetMapping("/makeAppointment/api/updateMakeAppointmentById")
    public Result updateMakeAppointmentById(@RequestParam("makeAppointmentId") Long makeAppointmentId,
                                            @RequestParam("status") String status);

    @GetMapping("/makeAppointment/api/findComplete")
    public Integer findComplete(@RequestParam("tutorId") Long tutorId);

//    @GetMapping("getMakeAppointmentByIdFromTutor")
//    public MakeAppointment getMakeAppointmentByIdFromTutor(@RequestParam("userId") Long userId);

}

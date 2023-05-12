package com.jia.dxssmjj.tutor.client;

import com.jia.dxssmjj.model.po.tutor.TutorSet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "service-tutor")
@Component
public interface TutorFeignClient {

    @PostMapping("/tutor/api/findById")
    public TutorSet findById(@RequestParam("tutorId") Long tutorId);

    @PostMapping("/tutor/api/findTutorIdByUserId")
    public TutorSet findTutorIdByUserId(@RequestParam("userId") Long userId);


    @PutMapping("/tutor/api/updateHourPrice")
    public Integer updateHourPrice(@RequestParam("tutorId") Long tutorId,@RequestParam("hourPrice") BigDecimal hourPrice);

}

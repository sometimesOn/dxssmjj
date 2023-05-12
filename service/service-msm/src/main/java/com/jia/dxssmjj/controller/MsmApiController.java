package com.jia.dxssmjj.controller;

import com.jia.dxssmjj.common.helper.ValidateCodeUtils;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.dto.UserLoginDTO;
import com.jia.dxssmjj.service.MsmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("/msm/api")
public class MsmApiController {

    @Autowired
    private MsmService msmService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @PostMapping("sendCode")
    public Result sendCode(@RequestBody UserLoginDTO userLoginDTO){

        String code = redisTemplate.opsForValue().get(userLoginDTO.getPhone());
        if(StringUtils.hasText(code)){
            return Result.ok();
        }

        code = ValidateCodeUtils.generateValidateCode(6).toString();
        boolean isSend = msmService.send(userLoginDTO.getPhone(),code);

        if(isSend){
            redisTemplate.opsForValue().set(userLoginDTO.getPhone(),code,5, TimeUnit.MINUTES);
            return Result.ok();
        }
        else {
            return Result.fail("发送短信失败");
        }

    }

}

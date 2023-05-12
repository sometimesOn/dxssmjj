package com.jia.dxssmjj.user.client;

import com.jia.dxssmjj.model.po.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-user")
@Component
public interface UserFeignClient {

    @GetMapping("/user/api/getUser")
    public User getUser(@RequestParam("userId") Long userId);

}

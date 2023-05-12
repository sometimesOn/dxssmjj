package com.jia.dxssmjj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.dxssmjj.common.result.Result;
import com.jia.dxssmjj.model.dto.LogonDTO;
import com.jia.dxssmjj.model.dto.UserAuthDTO;
import com.jia.dxssmjj.model.dto.UserLoginDTO;
import com.jia.dxssmjj.model.po.user.User;

import java.util.Map;

public interface UserService extends IService<User> {
    Map<String, Object> loginUser(UserLoginDTO userLoginDTO);

    void userAuth(Long userId, UserAuthDTO authDTO);

    Result logon(LogonDTO logonDTO);

    Result payment(Long makeAppointmentId, Long userId);

    Result confirm(Long makeAppointmentId);
}

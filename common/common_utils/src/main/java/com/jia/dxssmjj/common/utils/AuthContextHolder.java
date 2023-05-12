package com.jia.dxssmjj.common.utils;

import com.jia.dxssmjj.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

public class AuthContextHolder {

    public static Long getUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }

    public static Long getUserName(HttpServletRequest request){
        String name = request.getHeader("name");
        Long userName = JwtHelper.getUserId(name);
        return userName;
    }
}

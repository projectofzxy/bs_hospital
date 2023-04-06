package com.zxy.common.utils;

import com.zxy.common.helper.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : AuthContextHolder  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/20  15:47
 */
public class AuthContextHolder {
    //获取当前用户id
    public static Long getUserId(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        Long userId = JwtHelper.getUserId(token);
        return userId;
    }
    //获取当前用户名称
    public static String getUserName(HttpServletRequest request) {
        //从header获取token
        String token = request.getHeader("token");
        //jwt从token获取userid
        String userName = JwtHelper.getUserName(token);
        return userName;
    }

}

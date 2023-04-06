package com.zxy.user.api;

import com.zxy.common.result.Result;
import com.zxy.common.utils.AuthContextHolder;
import com.zxy.user.service.UserInfoService;
import com.zxy.yygh.model.user.UserInfo;
import com.zxy.yygh.vo.user.LoginVo;
import com.zxy.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName : UserInfoApiController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/13  22:30
 */
@RestController
@RequestMapping("/api/user")
public class UserInfoApiController {
    @Autowired
    private UserInfoService userInfoService;
    @ApiOperation(value = "会员登录")
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        Map<String,Object> info=userInfoService.loginUser(loginVo);
        return Result.ok(info);

    }
    //用户认证接口
    @PostMapping("auth/userAuth")
    public Result userAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        //传递两个参数，第一个参数用户id，第二个参数认证数据vo对象
        userInfoService.userAuth(AuthContextHolder.getUserId(request),userAuthVo);
        return Result.ok();
    }

    //获取用户id信息接口
    @GetMapping("auth/getUserInfo")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        UserInfo userInfo = userInfoService.getById(userId);
        return Result.ok(userInfo);
    }

}

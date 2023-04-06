package com.zxy.common.exception;

import com.zxy.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName : GlobalExceptionHandler  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/1  18:24
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    @ResponseBody
    @ExceptionHandler(YyghException.class)
    public Result error(YyghException e) {
        e.printStackTrace();
        return Result.fail();
    }
}

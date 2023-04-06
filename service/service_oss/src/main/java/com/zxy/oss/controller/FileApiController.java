package com.zxy.oss.controller;

import com.zxy.common.result.Result;
import com.zxy.oss.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName : FileApiController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/20  15:07
 */
@RestController
@RequestMapping("/api/oss/file")
public class FileApiController {
    @Autowired
    FileService fileService;
    @PostMapping("fileUpload")
    public Result fileUpload(MultipartFile file){
        String url=fileService.upload(file);
        return Result.ok(url);
    }
}

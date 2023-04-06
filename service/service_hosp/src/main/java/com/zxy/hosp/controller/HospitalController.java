package com.zxy.hosp.controller;


import com.zxy.common.result.Result;
import com.zxy.hosp.sercice.HospitalService;
import com.zxy.yygh.model.hosp.Hospital;
import com.zxy.yygh.vo.hosp.HospitalQueryVo;
import com.zxy.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @ClassName : HospitalController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/11  15:34
 */
@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "获取医院详情")
    @GetMapping("showHosDetail/{id}")
    public Result showHosDetail(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable String id) {
        Map<String, Object> map = hospitalService.getHosById(id);
        return Result.ok(map);
    }

    @ApiOperation(value = "更新上线状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result lock(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable("id") String id,
            @ApiParam(name = "status", value = "状态（0：未上线 1：已上线）", required = true)
            @PathVariable("status") Integer status){
        hospitalService.updateStatus(id, status);
        return Result.ok();
    }

    //医院列表
    @GetMapping("list/{page}/{limit}")
    public Result listHosp(@PathVariable Integer page, @PathVariable Integer limit,
                           HospitalQueryVo hospitalQueryVo){
        Page<Hospital> pageModel=hospitalService.selectHospPage(page,limit,hospitalQueryVo);
        return Result.ok(pageModel);
    }
}

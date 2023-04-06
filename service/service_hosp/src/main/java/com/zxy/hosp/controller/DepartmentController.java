package com.zxy.hosp.controller;

import com.zxy.common.result.Result;
import com.zxy.hosp.sercice.DepartmentService;
import com.zxy.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName : DepartmentController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/12  14:05
 */
@RestController
@RequestMapping("/admin/hosp/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    //根据医院编号，查询医院所有科室列表
    @ApiOperation(value = "查询医院所有科室列表")
    @GetMapping("getDeptList/{hoscode}")
    public Result getDeptList(@PathVariable String hoscode) {
        List<DepartmentVo> list=departmentService.findDeptTree(hoscode);
        return Result.ok(list);
    }

}

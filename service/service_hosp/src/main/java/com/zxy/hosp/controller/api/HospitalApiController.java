package com.zxy.hosp.controller.api;

import com.zxy.common.result.Result;
import com.zxy.hosp.sercice.DepartmentService;
import com.zxy.hosp.sercice.HospitalService;
import com.zxy.hosp.sercice.HospitalSetService;
import com.zxy.hosp.sercice.ScheduleService;
import com.zxy.yygh.model.hosp.Hospital;
import com.zxy.yygh.vo.hosp.HospitalQueryVo;
import com.zxy.yygh.vo.order.SignInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : HospitalApiController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/13  15:10
 */
@Api(tags = "医院管理接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @ApiOperation(value = "获取医院签名信息")
    @GetMapping("inner/getSignInfoVo/{hoscode}")
    public SignInfoVo getSignInfoVo(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable("hoscode") String hoscode) {
        return hospitalSetService.getSignInfoVo(hoscode);
    }

    @ApiOperation(value = "根据排班id获取排班数据")
    @GetMapping("getSchedule/{scheduleId}")
    public Result getSchedule(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable String scheduleId) {
        return Result.ok(scheduleService.getById(scheduleId));
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public Result getBookingSchedule(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode) {
        return Result.ok(scheduleService.getBookingScheduleRule(page, limit, hoscode, depcode));
    }

    @ApiOperation(value = "获取排班数据")
    @GetMapping("auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public Result findScheduleList(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode,
            @ApiParam(name = "depcode", value = "科室code", required = true)
            @PathVariable String depcode,
            @ApiParam(name = "workDate", value = "排班日期", required = true)
            @PathVariable String workDate) {
        return Result.ok(scheduleService.getDetailSchedule(hoscode, depcode, workDate));
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("findHospList/{page}/{limit}")
    public Result findHospList(@PathVariable Integer page,
                               @PathVariable Integer limit,
                               HospitalQueryVo hospitalQueryVo) {
        Page<Hospital> hospitals = hospitalService.selectHospPage(page, limit, hospitalQueryVo);
        return Result.ok(hospitals);
    }

    @ApiOperation(value = "根据医院名称获取医院列表")
    @GetMapping("findByHosname/{hosname}")
    public Result findByHosname(
            @ApiParam(name = "hosname", value = "医院名称", required = true)
            @PathVariable String hosname) {
        return Result.ok(hospitalService.findByHosname(hosname));
    }
    @ApiOperation(value = "获取科室列表")
    @GetMapping("department/{hoscode}")
    public Result index(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode) {
        return Result.ok(departmentService.findDeptTree(hoscode));
    }

    @ApiOperation(value = "医院预约挂号详情")
    @GetMapping("findHospDetail/{hoscode}")
    public Result item(
            @ApiParam(name = "hoscode", value = "医院code", required = true)
            @PathVariable String hoscode) {
        return Result.ok(hospitalService.item(hoscode));
    }

}

package com.zxy.hosp.controller.api;


import com.zxy.common.exception.YyghException;
import com.zxy.common.helper.HttpRequestHelper;
import com.zxy.common.result.Result;
import com.zxy.common.result.ResultCodeEnum;
import com.zxy.common.utils.MD5;
import com.zxy.hosp.sercice.DepartmentService;
import com.zxy.hosp.sercice.HospitalService;
import com.zxy.hosp.sercice.HospitalSetService;
import com.zxy.hosp.sercice.ScheduleService;
import com.zxy.yygh.model.hosp.Department;
import com.zxy.yygh.model.hosp.Hospital;
import com.zxy.yygh.model.hosp.Schedule;
import com.zxy.yygh.vo.hosp.DepartmentQueryVo;
import com.zxy.yygh.vo.hosp.ScheduleOrderVo;
import com.zxy.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ClassName : ApiController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/10  16:01
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("hospital/inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(
            @ApiParam(name = "scheduleId", value = "排班id", required = true)
            @PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    //上传医院接口
    @PostMapping("saveHospital")
    public Result saveHosp(HttpServletRequest request){

        //获取传递信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);
        //签名校验
        String sign = (String) objectMap.get("sign");
        String hoscode = (String) objectMap.get("hoscode");
        String signkey=hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signkey);
        if (!sign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        String logoData = (String) objectMap.get("logoData");
        logoData=logoData.replaceAll(" ","+");
        objectMap.put("logoData",logoData);

        hospitalService.save(objectMap);
        return Result.ok();
    }

    //查询医院
    @PostMapping("hospital/show")
    public Result getHospital(HttpServletRequest request){
        //获取传递信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);
        //签名校验
        String sign = (String) objectMap.get("sign");
        String hoscode = (String) objectMap.get("hoscode");
        String signkey=hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signkey);
        if (!sign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }


        Hospital hospital=hospitalService.getByHosCode(hoscode);
        return Result.ok(hospital);
    }

    //上传科室接口
    @PostMapping("saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        //获取传递信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);
        //签名校验
        String sign = (String) objectMap.get("sign");
        String hoscode = (String) objectMap.get("hoscode");
        String signkey=hospitalSetService.getSignKey(hoscode);
        String signKeyMD5 = MD5.encrypt(signkey);
        if (!sign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        departmentService.save(objectMap);
        return Result.ok();
    }



    //查询科室接口
    @PostMapping("department/list")
    public Result findDepartment(HttpServletRequest request){
        //获取传递信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) objectMap.get("hoscode");



        int page = StringUtils.isEmpty(objectMap.get("page")) ? 1 : Integer.parseInt((String) objectMap.get("page"));
        int limit = StringUtils.isEmpty(objectMap.get("limit")) ? 1 : Integer.parseInt((String) objectMap.get("limit"));

        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        Page<Department> pageModel=departmentService.findPageDepartment(page,limit,departmentQueryVo);
        return Result.ok(pageModel);

    }
    //删除科室接口
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request){
        //获取传递信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) objectMap.get("hoscode");

        String depcode = (String) objectMap.get("depcode");
        departmentService.remove(hoscode,depcode);
        return Result.ok();
    }
    //上传排班
    @PostMapping("saveSchedule")
    public Result saveSchedule(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);
        //TODO 签名校验
        scheduleService.save(objectMap);
        return Result.ok();
    }
    //查询排班
    @PostMapping("schedule/list")
    public Result findSchedule(HttpServletRequest request){
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);
        //TODO 签名校验
        String hoscode = (String) objectMap.get("hoscode");
        String depcode = (String) objectMap.get("depcode");
        int page = StringUtils.isEmpty(objectMap.get("page")) ? 1 : Integer.parseInt((String) objectMap.get("page"));
        int limit = StringUtils.isEmpty(objectMap.get("limit")) ? 1 : Integer.parseInt((String) objectMap.get("limit"));

        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel=scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
        return Result.ok(pageModel);
    }
    //删除排班
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request){
        //获取传递信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> objectMap = HttpRequestHelper.switchMap(requestMap);

        String hoscode = (String) objectMap.get("hoscode");
        String hosScheduleId = (String) objectMap.get("hosScheduleId");
        scheduleService.remove(hoscode,hosScheduleId);
        return Result.ok();
    }
}

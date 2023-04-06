package com.zxy.user.api;

import com.zxy.common.result.Result;
import com.zxy.common.utils.AuthContextHolder;
import com.zxy.user.service.PatientService;
import com.zxy.yygh.model.user.Patient;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ClassName : PatientApiController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/20  19:11
 */
@RestController
@RequestMapping("/api/user/patient")
public class PatientApiController {
    //就诊人CRUD
    @Autowired
    private PatientService patientService;
    @GetMapping("auth/findAll")
    public Result findAll(HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list=patientService.findAllByUserId(userId);
        return Result.ok(list);
    }

    @PostMapping("auth/save")
    public Result savePatient(@RequestBody Patient patient,HttpServletRequest request){
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return Result.ok();
    }

    @GetMapping("auth/get/{id}")
    public Result getPatient(@PathVariable Long id){
        Patient patient=patientService.getPatientId(id);
        return Result.ok(patient);
    }

    @PostMapping("auth/update")
    public Result updatePatient(@RequestBody Patient patient){
        patientService.updateById(patient);
        return Result.ok();
    }

    @DeleteMapping("auth/remove/{id}")
    public  Result removePatient(@PathVariable Long id){
        patientService.removeById(id);
        return Result.ok();
    }
    @ApiOperation(value = "获取就诊人")
    @GetMapping("inner/get/{id}")
    public Patient getPatientOrder(
            @ApiParam(name = "id", value = "就诊人id", required = true)
            @PathVariable("id") Long id) {
        return patientService.getPatientId(id);
    }

}

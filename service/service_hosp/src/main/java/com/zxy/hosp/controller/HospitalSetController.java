package com.zxy.hosp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxy.common.result.Result;
import com.zxy.common.utils.MD5;
import com.zxy.hosp.sercice.HospitalSetService;
import com.zxy.yygh.model.hosp.HospitalSet;
import com.zxy.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * @ClassName : HospitalSetController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/1  15:46
 */
@Api(tags = "医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    //1 查询医院设置表所有信息
    @ApiOperation(value = "获取所有医院设置")
    @GetMapping("findAll")
    public Result findAllHosptialSet(){
        List<HospitalSet> list = hospitalSetService.list();
        return Result.ok(list);
    }
    //2 逻辑删除医院设置
    @ApiOperation(value = "逻辑删除医院设置")
    @DeleteMapping("{id}")
    public Result removeHospSet(@PathVariable Long id){
        boolean flag = hospitalSetService.removeById(id);
        if (flag)
        {
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //3 条件查询带分页
    @PostMapping("findPageHospSet/{current}/{limit}")
    public Result findPageHospSet(@PathVariable long current, @PathVariable long limit,
                                  @RequestBody(required = false)//参数可以为空
                                          HospitalSetQueryVo hospitalSetQueryVo){
        Page<HospitalSet> page=new Page<>(current,limit);
        QueryWrapper<HospitalSet> wrapper=new QueryWrapper<>();
        String hosname = hospitalSetQueryVo.getHosname();
        String hoscode = hospitalSetQueryVo.getHoscode();
        if (!StringUtils.isEmpty(hosname)){
            wrapper.like("hosname",hosname);
        }
        if (!StringUtils.isEmpty(hoscode)){
            wrapper.eq("hoscode",hoscode);
        }

        Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page, wrapper);
        return Result.ok(hospitalSetPage);
    }
    //4 添加医院设置
    @PostMapping("saveHospitalSet")
    public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet) {
        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //生成密钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        boolean save = hospitalSetService.save(hospitalSet);

        if (save){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //5 根据id获取医院设置
    @GetMapping("getHospSet/{id}")
    public Result getHospSet(@PathVariable Long id) {
        //        try {
//            //模拟异常
//            int a = 1/0;
//        }catch (Exception e) {
//            throw new YyghException("失败",201);
//        }
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        return Result.ok(hospitalSet);
    }
    //6 修改医院设置
    @PostMapping("updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet) {
        boolean flag = hospitalSetService.updateById(hospitalSet);
        if (flag){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }
    //7 批量删除医院设置
    @DeleteMapping("batchRemove")
    public Result batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return Result.ok();
    }
    //8 医院设置锁定和解锁
    @PutMapping("lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,
                                  @PathVariable Integer status) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        hospitalSet.setStatus(status);
        hospitalSetService.updateById(hospitalSet);
        return Result.ok();
    }
    //9 发送签名秘钥
    @PutMapping("sendKey/{id}")
    public Result lockHospitalSet(@PathVariable Long id) {
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO 发送短信
        return Result.ok();
    }
}

package com.zxy.hosp.sercice.impl;

import com.alibaba.fastjson.JSONObject;
import com.zxy.cmn.client.DictFeignClient;
import com.zxy.hosp.repository.HospitalRepository;
import com.zxy.hosp.sercice.HospitalService;
import com.zxy.yygh.model.hosp.Hospital;
import com.zxy.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : HospitalServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/10  16:00
 */
@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;
    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> objectMap) {
        //吧map集合转换为对象
        String jsonString = JSONObject.toJSONString(objectMap);
        Hospital hospital = JSONObject.parseObject(jsonString, Hospital.class);
        //判断是否存在
        String hoscode = hospital.getHoscode();
        Hospital hospitalExist=hospitalRepository.getHospitalByHoscode(hoscode);
        //如果存在，添加
        if (hospitalExist!=null){
            hospital.setStatus(hospitalExist.getStatus());
            hospital.setCreateTime(hospitalExist.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {  //不存在，进行修改
            hospital.setStatus(0);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getByHosCode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }

    @Override
    public Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Pageable pageable= PageRequest.of(page-1,limit);
        ExampleMatcher matcher=ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnoreCase(true);
        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo,hospital);
        Example<Hospital> example=Example.of(hospital,matcher);
        Page<Hospital> hospitals = hospitalRepository.findAll(example, pageable);
        //获取查询list集合，遍历进行医院等级封装
        hospitals.getContent().stream().forEach(item->{
            this.setHospitalHosType(item);
        });
        return hospitals;
    }

    /**
     * 更新上线状态
     */
    @Override
    public void updateStatus(String id, Integer status) {
        if(status.intValue() == 0 || status.intValue() == 1) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }
    /**
     * 医院详情
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> getHosById(String id) {
        Hospital hospital = this.setHospitalHosType(hospitalRepository.findById(id).get());
        Map<String, Object> result = new HashMap<>();
        result.put("hospital", hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        if(null != hospital) {
            return hospital.getHosname();
        }
        return "";
    }

    @Override
    public List<Hospital> findByHosname(String hosname) {
        return hospitalRepository.findHospitalByHosnameLike(hosname);
    }

    @Override
    public Map<String, Object> item(String hoscode) {
        Map<String, Object> result = new HashMap<>();
        //医院详情
        Hospital hospital = this.setHospitalHosType(this.getByHosCode(hoscode));
        result.put("hospital", hospital);
        //预约规则
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;

    }


    //获取查询list集合，遍历进行医院等级封装
    private Hospital setHospitalHosType(Hospital hospital) {
        String hostypeString = dictFeignClient.getName("hostype", hospital.getHostype());
        //查询省市区
        String provinceString = dictFeignClient.getName("",hospital.getProvinceCode());
        String cityString = dictFeignClient.getName("",hospital.getCityCode());
        String districtString = dictFeignClient.getName("",hospital.getDistrictCode());
        hospital.getParam().put("fullAddress",provinceString+cityString+districtString);
        hospital.getParam().put("hostypeString",hostypeString);
        return hospital;
    }
}

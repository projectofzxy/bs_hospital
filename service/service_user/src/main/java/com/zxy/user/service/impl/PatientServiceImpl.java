package com.zxy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxy.cmn.client.DictFeignClient;
import com.zxy.user.mapper.PatientMapper;
import com.zxy.user.service.PatientService;
import com.zxy.yygh.enums.DictEnum;
import com.zxy.yygh.model.user.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : PatientServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/20  19:13
 */
@Service
public class PatientServiceImpl extends ServiceImpl<PatientMapper,Patient> implements PatientService {
    @Autowired
    private DictFeignClient dictFeignClient;
    @Override
    public List<Patient> findAllByUserId(Long userId) {
        QueryWrapper<Patient> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<Patient> patientList = baseMapper.selectList(wrapper);
        patientList.stream().forEach(item->{
            this.packPatient(item);
        });
        return patientList;
    }

    @Override
    public Patient getPatientId(Long id) {
        return this.packPatient(baseMapper.selectById(id));
    }

    //封装
    private Patient packPatient(Patient patient) {
        //根据证件类型编码，获取证件类型具体指
        String certificatesTypeString = dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(), patient.getCertificatesType());
        //联系人证件类型
        String contactsCertificatesTypeString =
                dictFeignClient.getName(DictEnum.CERTIFICATES_TYPE.getDictCode(),patient.getContactsCertificatesType());
        //省
        String provinceString = dictFeignClient.getName(patient.getProvinceCode());
        //市
        String cityString = dictFeignClient.getName(patient.getCityCode());
        //区
        String districtString = dictFeignClient.getName(patient.getDistrictCode());
        patient.getParam().put("certificatesTypeString", certificatesTypeString);
        patient.getParam().put("contactsCertificatesTypeString", contactsCertificatesTypeString);
        patient.getParam().put("provinceString", provinceString);
        patient.getParam().put("cityString", cityString);
        patient.getParam().put("districtString", districtString);
        patient.getParam().put("fullAddress", provinceString + cityString + districtString + patient.getAddress());
        return patient;

    }
}

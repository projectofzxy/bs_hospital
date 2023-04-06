package com.zxy.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxy.yygh.model.user.Patient;

import java.util.List;

public interface PatientService extends IService<Patient> {
    List<Patient> findAllByUserId(Long userId);

    Patient getPatientId(Long id);
}

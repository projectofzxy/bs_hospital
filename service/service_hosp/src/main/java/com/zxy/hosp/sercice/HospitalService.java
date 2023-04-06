package com.zxy.hosp.sercice;

import com.zxy.yygh.model.hosp.Hospital;
import com.zxy.yygh.vo.hosp.HospitalQueryVo;
import com.zxy.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface HospitalService {
    void save(Map<String, Object> objectMap);

    Hospital getByHosCode(String hoscode);

    Page<Hospital> selectHospPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> getHosById(String id);

    String getHospName(String hoscode);

    List<Hospital> findByHosname(String hosname);

    Map<String,Object> item(String hoscode);
}

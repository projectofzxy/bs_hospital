package com.zxy.hosp.sercice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zxy.yygh.model.hosp.HospitalSet;
import com.zxy.yygh.vo.order.SignInfoVo;

public interface HospitalSetService extends IService<HospitalSet> {
    String getSignKey(String hoscode);

    SignInfoVo getSignInfoVo(String hoscode);
}

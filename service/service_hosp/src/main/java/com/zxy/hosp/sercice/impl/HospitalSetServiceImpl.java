package com.zxy.hosp.sercice.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxy.common.exception.YyghException;
import com.zxy.common.result.ResultCodeEnum;
import com.zxy.hosp.mapper.HospitalSetMapper;
import com.zxy.hosp.sercice.HospitalSetService;
import com.zxy.yygh.model.hosp.HospitalSet;
import com.zxy.yygh.vo.order.SignInfoVo;
import org.springframework.stereotype.Service;

/**
 * @ClassName : HospitalSetServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/1  15:43
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {

    @Override
    public String getSignKey(String hoscode) {
        QueryWrapper<HospitalSet> wrapper=new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        return hospitalSet.getSignKey();
    }

    @Override
    public SignInfoVo getSignInfoVo(String hoscode) {
        QueryWrapper<HospitalSet> wrapper = new QueryWrapper<>();
        wrapper.eq("hoscode",hoscode);
        HospitalSet hospitalSet = baseMapper.selectOne(wrapper);
        if(null == hospitalSet) {
            throw new YyghException(ResultCodeEnum.HOSPITAL_OPEN);
        }
        SignInfoVo signInfoVo = new SignInfoVo();
        signInfoVo.setApiUrl(hospitalSet.getApiUrl());
        signInfoVo.setSignKey(hospitalSet.getSignKey());
        return signInfoVo;

    }
}

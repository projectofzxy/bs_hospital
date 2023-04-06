package com.zxy.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxy.common.exception.YyghException;
import com.zxy.common.helper.JwtHelper;
import com.zxy.common.result.ResultCodeEnum;
import com.zxy.user.mapper.UserInfoMapper;
import com.zxy.user.service.PatientService;
import com.zxy.user.service.UserInfoService;
import com.zxy.yygh.enums.AuthStatusEnum;
import com.zxy.yygh.model.user.Patient;
import com.zxy.yygh.model.user.UserInfo;
import com.zxy.yygh.vo.user.LoginVo;
import com.zxy.yygh.vo.user.UserAuthVo;
import com.zxy.yygh.vo.user.UserInfoQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : UserInfoServiceImpl  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/13  22:32
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Autowired
    private PatientService patientService;
    @Override
    public Map<String, Object> loginUser(LoginVo loginVo) {
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        if (StringUtils.isEmpty(phone)||StringUtils.isEmpty(code)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        // 判断手机验证码和输入验证码是否一致
        String redisDode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisDode)){
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
//绑定手机号码
        UserInfo userInfo = null;
        if(!StringUtils.isEmpty(loginVo.getOpenid())) {
            userInfo = userInfoService.selectWxInfoOpenId(loginVo.getOpenid());
            if(null != userInfo) {
                userInfo.setPhone(loginVo.getPhone());
                userInfoService.updateById(userInfo);
            } else {
                throw new YyghException(ResultCodeEnum.DATA_ERROR);
            }
        }
//userInfo=null 说明手机直接登录
        if(null == userInfo) {
            QueryWrapper<UserInfo> wrapper=new QueryWrapper<>();
            wrapper.eq("phone",phone);
            userInfo = baseMapper.selectOne(wrapper);
            if (userInfo==null){
                userInfo = new UserInfo();
                userInfo.setName("");
                userInfo.setPhone(phone);
                userInfo.setStatus(1);
                userInfo.setUserAuth("common");
                baseMapper.insert(userInfo);
            }
        }


        if (userInfo.getStatus()==0){
            throw new YyghException(ResultCodeEnum.LOGIN_DISABLED_ERROR);
        }
        //TODO 记录登录

        //返回页面显示名称
        Map<String, Object> map = new HashMap<>();
        String name = userInfo.getName();
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if(StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getName());
        map.put("name", name);
        map.put("token", token);
        return map;
    }

    @Override
    public UserInfo selectWxInfoOpenId(String openid) {
        QueryWrapper<UserInfo> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("openid",openid);
        UserInfo userInfo = baseMapper.selectOne(queryWrapper);
        return userInfo;
    }

    @Override
    public void userAuth(Long userId, UserAuthVo userAuthVo) {
        //根据用户id查询用户信息
        UserInfo userInfo = baseMapper.selectById(userId);
        //设置认证信息
        //认证人姓名
        userInfo.setName(userAuthVo.getName());
        //其他认证信息
        userInfo.setCertificatesType(userAuthVo.getCertificatesType());
        userInfo.setCertificatesNo(userAuthVo.getCertificatesNo());
        userInfo.setCertificatesUrl(userAuthVo.getCertificatesUrl());
        userInfo.setAuthStatus(AuthStatusEnum.AUTH_RUN.getStatus());
        //进行信息更新
        baseMapper.updateById(userInfo);

    }

    @Override
    public IPage<UserInfo> selectPage(Page<UserInfo> pageParam, UserInfoQueryVo userInfoQueryVo) {
        String keyword = userInfoQueryVo.getKeyword();//用户名称
        Integer status = userInfoQueryVo.getStatus();
        Integer authStatus = userInfoQueryVo.getAuthStatus();//认证状态
        String createTimeBegin = userInfoQueryVo.getCreateTimeBegin();
        String createTimeEnd = userInfoQueryVo.getCreateTimeEnd();
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(keyword)){
            wrapper.like("name",keyword);
        }
        if (!StringUtils.isEmpty(status)){
            wrapper.eq("status",status);
        }
        if (!StringUtils.isEmpty(authStatus)){
            wrapper.eq("auth_status",authStatus);
        }
        if (!StringUtils.isEmpty(createTimeBegin)){
            wrapper.ge("create_time",createTimeBegin);//>=
        }
        if (!StringUtils.isEmpty(createTimeEnd)){
            wrapper.le("end_time",createTimeEnd);//<=
        }
        Page<UserInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().stream().forEach(item->{
            this.packageUserInfo(item);
        });
        return pages;
    }

    @Override
    public void lock(Long userId, Integer status) {
        if (status.intValue()==0||status.intValue()==1){
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setStatus(status);
            baseMapper.updateById(userInfo);
        }
    }

    @Override
    public Map<String, Object> show(Long userId) {
        Map<String,Object> map = new HashMap<>();
        //根据userid查询用户信息
        UserInfo userInfo =this.packageUserInfo( baseMapper.selectById(userId));
        map.put("userInfo",userInfo);
        //根据userid查询就诊人信息
        List<Patient> patientList = patientService.findAllByUserId(userId);
        map.put("patientList",patientList);
        return map;

    }

    @Override
    public void approval(Long userId, Integer authStatus) {
        //2表示通过，-1表示不通过
        if(authStatus.intValue()==2 || authStatus.intValue()==-1) {
            UserInfo userInfo = baseMapper.selectById(userId);
            userInfo.setAuthStatus(authStatus);
            baseMapper.updateById(userInfo);
        }
    }

    //编号变成对应值封装
    private UserInfo packageUserInfo(UserInfo userInfo) {
        userInfo.getParam().put("authStatusString",AuthStatusEnum.getStatusNameByStatus(userInfo.getAuthStatus()));
        String status = userInfo.getStatus().intValue()==0?"锁定":"正常";
        userInfo.getParam().put("statusString",status);
        return userInfo;
    }

}

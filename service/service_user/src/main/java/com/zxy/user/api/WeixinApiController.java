package com.zxy.user.api;

import com.alibaba.fastjson.JSONObject;
import com.zxy.common.helper.JwtHelper;
import com.zxy.common.result.Result;
import com.zxy.user.service.UserInfoService;
import com.zxy.user.utils.ConstantWxPropertiesUtils;
import com.zxy.user.utils.HttpClientUtils;
import com.zxy.yygh.model.user.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName : WeixinApiController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/19  15:12
 */
@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinApiController {
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 微信登录回调
     *
     * @param code
     * @param state
     * @return
     */
    @RequestMapping("callback")
    public String callback(String code, String state) {
        //获取授权临时票据
        System.out.println("微信授权服务器回调。。。。。。");
        System.out.println("state = " + state);
        System.out.println("code = " + code);
        //使用code和appid以及appscrect换取access_token
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
                code);
        try {
            String accesstokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("使用code换取的access_token结果 = " + accesstokenInfo);

            JSONObject jsonObject = JSONObject.parseObject(accesstokenInfo);
            String accessToken = jsonObject.getString("access_token");
            String openid = jsonObject.getString("openid");

            UserInfo userInfo=userInfoService.selectWxInfoOpenId(openid);
            if (userInfo==null){
                //使用access_token换取受保护的资源：微信的个人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
                String resultInfo = HttpClientUtils.get(userInfoUrl);
                System.out.println(resultInfo);
                JSONObject object = JSONObject.parseObject(resultInfo);
                //解析用户信息
                String nickname = object.getString("nickname");
                String headimgurl = object.getString("headimgurl");
                userInfo = new UserInfo();
                userInfo.setNickName(nickname);
                userInfo.setOpenid(openid);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }


            Map<String,String> map=new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token",token);

        return  "redirect:" + ConstantWxPropertiesUtils.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+"&openid="+map.get("openid")+"&name="+URLEncoder.encode(map.get("name"),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

        /**
         * 获取微信登录参数
         */
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect(HttpSession session){
        try {
        HashMap<String, Object> map = new HashMap<>();
        map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);
        map.put("scope","snsapi_login");
        String wxOpenRedirectUrl = null;
            wxOpenRedirectUrl = URLEncoder.encode(ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL,"utf-8");
        map.put("redirect_uri",wxOpenRedirectUrl);
        map.put("state",System.currentTimeMillis()+"");
        return Result.ok(map);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}


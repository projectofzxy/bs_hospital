package com.zxy.order.api;

import com.zxy.common.result.Result;
import com.zxy.order.service.PaymentService;
import com.zxy.order.service.WeixinService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName : WeixinController  //类名
 * @Description :   //描述
 * @Author : 10079 //作者
 * @Date: 2023/2/22  21:35
 */
@RestController
@RequestMapping("/api/order/weixin")
public class WeixinController {
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private PaymentService paymentService;
    @GetMapping("createNative/{orderId}")
    public Result createNative(@PathVariable Long orderId){
        Map map= null;
        try {
            map = weixinService.createNative(orderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.ok(map);
    }

    @ApiOperation(value = "查询支付状态")
    @GetMapping("/queryPayStatus/{orderId}")
    public Result queryPayStatus(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable("orderId") Long orderId) {
        //调用查询接口
        Map<String,String> resultMap=weixinService.queryPayStatus(orderId);
        if (resultMap==null)
        {
            return Result.fail().message("支付出错");
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))){
            String out_trade_no = resultMap.get("out_trade_no");
            paymentService.paySuccess(out_trade_no,resultMap);
            return Result.ok().message("支付成功");
        }
        return Result.ok().message("支付中");
    }

}

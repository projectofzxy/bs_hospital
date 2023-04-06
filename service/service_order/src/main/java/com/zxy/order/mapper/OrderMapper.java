package com.zxy.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zxy.yygh.model.order.OrderInfo;
import com.zxy.yygh.vo.order.OrderCountQueryVo;
import com.zxy.yygh.vo.order.OrderCountVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper extends BaseMapper<OrderInfo> {
        List<OrderCountVo> selectOrderCount(@Param("vo") //别名
                                                    OrderCountQueryVo orderCountQueryVo);
}

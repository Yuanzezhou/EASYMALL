package com.harbin.mymall.mymallorder.dao;

import com.harbin.mymall.mymallorder.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:13
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    void updateOrderStatus(@Param("outTradeNo") String outTradeNo, @Param("code")Integer code);

}

package com.harbin.mymall.mymallorder.dao;

import com.harbin.mymall.mymallorder.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:13
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}

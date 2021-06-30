package com.harbin.mymall.mymallorder.dao;

import com.harbin.mymall.mymallorder.entity.OrderOperateHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单操作历史记录
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:12
 */
@Mapper
public interface OrderOperateHistoryDao extends BaseMapper<OrderOperateHistoryEntity> {
	
}

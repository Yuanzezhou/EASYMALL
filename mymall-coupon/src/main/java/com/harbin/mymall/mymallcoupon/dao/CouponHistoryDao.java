package com.harbin.mymall.mymallcoupon.dao;

import com.harbin.mymall.mymallcoupon.entity.CouponHistoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券领取历史记录
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 23:10:44
 */
@Mapper
public interface CouponHistoryDao extends BaseMapper<CouponHistoryEntity> {
	
}

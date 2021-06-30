package com.harbin.mymall.mymallware.dao;

import com.harbin.mymall.mymallware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品库存
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:53
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    Long getSkuStock(Long skuId);

    List<Long> listWareIdHasSkuStock(@Param("skuId")Long skuId);

    Long lockSkuStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId,@Param("num") Integer num);

    void unLockStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("num") Integer num, @Param("taskDetailId") Long taskDetailId);
}

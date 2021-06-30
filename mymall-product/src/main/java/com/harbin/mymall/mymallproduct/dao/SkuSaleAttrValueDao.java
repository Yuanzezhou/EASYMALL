package com.harbin.mymall.mymallproduct.dao;

import com.harbin.mymall.mymallproduct.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harbin.mymall.mymallproduct.vo.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-02-05 15:45:23
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {

    List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(@Param("spuId") Long spuId);

    List<String> getSkuSaleAttrValuesAsStringList(@Param("skuId") Long skuId);
}

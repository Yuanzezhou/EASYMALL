package com.harbin.mymall.mymallproduct.vo;

import com.harbin.mymall.mymallproduct.entity.SkuImagesEntity;
import com.harbin.mymall.mymallproduct.entity.SkuInfoEntity;
import com.harbin.mymall.mymallproduct.entity.SpuInfoDescEntity;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-03-15:19
 */
@Data
public class SkuItemVo {

    //1、sku基本信息获取    pms_sku_info
    SkuInfoEntity info;

    Boolean hasStock = true;

    //2、sku的图片信息      pms_sku_images
    List<SkuImagesEntity> images;

    //3、获取spu的销售属性组合
    List<SkuItemSaleAttrVo> saleAttr;

    //4、获取spu的介绍
    SpuInfoDescEntity desc;

    //5、获取spu的规格参数信息
    List<SpuItemAttrGroupVo> groupAttrs;

    //6、秒杀信息
    SeckillInfoVo seckillInfoVo;

}

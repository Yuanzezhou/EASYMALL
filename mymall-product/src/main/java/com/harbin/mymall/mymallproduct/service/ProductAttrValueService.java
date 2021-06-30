package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveProductAttr(List<ProductAttrValueEntity> collect);

    List<ProductAttrValueEntity> listForSpu(Long spuId);

    void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities);
}


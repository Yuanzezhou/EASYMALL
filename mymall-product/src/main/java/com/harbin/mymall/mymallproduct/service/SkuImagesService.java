package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.SkuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * sku图片
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuImagesEntity> getImagesBySkuId(Long skuId);
}


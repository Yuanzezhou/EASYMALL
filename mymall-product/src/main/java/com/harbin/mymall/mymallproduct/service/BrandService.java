package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:07
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


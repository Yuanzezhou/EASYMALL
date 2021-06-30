package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.BrandEntity;
import com.harbin.mymall.mymallproduct.entity.CategoryBrandRelationEntity;
import com.harbin.mymall.mymallproduct.vo.BrandVo;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<BrandEntity> getBrandsByCatId(Long catId);

}


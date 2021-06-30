package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.CategoryEntity;
import com.harbin.mymall.mymallproduct.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    Long[] findCatelogPath(Long catelogId);

    List<CategoryEntity> getLevel1Categories();

    Map<String, List<Catelog2Vo>> getCatelogJson();

    Map<String, List<Catelog2Vo>> getCatelogJsonFromDbWithRedisLock();
}


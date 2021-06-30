package com.harbin.mymall.mymallproduct.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.SkuImagesDao;
import com.harbin.mymall.mymallproduct.entity.SkuImagesEntity;
import com.harbin.mymall.mymallproduct.service.SkuImagesService;


@Service("skuImagesService")
public class SkuImagesServiceImpl extends ServiceImpl<SkuImagesDao, SkuImagesEntity> implements SkuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuImagesEntity> page = this.page(
                new Query<SkuImagesEntity>().getPage(params),
                new QueryWrapper<SkuImagesEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuImagesEntity> getImagesBySkuId(Long skuId) {
        List<SkuImagesEntity> skuImagesEntities = this.baseMapper.selectList(new QueryWrapper<SkuImagesEntity>().eq("sku_id", skuId));
        return skuImagesEntities;

    }

}
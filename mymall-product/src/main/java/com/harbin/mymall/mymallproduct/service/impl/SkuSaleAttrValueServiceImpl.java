package com.harbin.mymall.mymallproduct.service.impl;

import com.harbin.mymall.mymallproduct.service.SkuImagesService;
import com.harbin.mymall.mymallproduct.vo.SkuItemSaleAttrVo;
import com.harbin.mymall.mymallproduct.vo.SkuItemVo.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.SkuSaleAttrValueDao;
import com.harbin.mymall.mymallproduct.entity.SkuSaleAttrValueEntity;
import com.harbin.mymall.mymallproduct.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {
        List<SkuItemSaleAttrVo> skuItemSaleAttrVos=  this.baseMapper.getSaleAttrsBySpuId(spuId);
        return skuItemSaleAttrVos;
    }

    @Override
    public List<String> getSkuSaleAttrValuesAsStringList(Long skuId) {
        SkuSaleAttrValueDao skuSaleAttrValueDao = this.baseMapper;
        return skuSaleAttrValueDao.getSkuSaleAttrValuesAsStringList(skuId);
    }
}
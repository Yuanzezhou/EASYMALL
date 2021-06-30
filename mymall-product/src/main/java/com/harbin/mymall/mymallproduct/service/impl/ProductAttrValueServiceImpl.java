package com.harbin.mymall.mymallproduct.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.ProductAttrValueDao;
import com.harbin.mymall.mymallproduct.entity.ProductAttrValueEntity;
import com.harbin.mymall.mymallproduct.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttr(List<ProductAttrValueEntity> collect) {
        if(!CollectionUtils.isEmpty(collect)){
            this.saveBatch(collect);
        }
    }

    @Override
    public List<ProductAttrValueEntity> listForSpu(Long spuId) {

        List<ProductAttrValueEntity> entities = this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return entities;
    }

    @Transactional
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<ProductAttrValueEntity> collect = entities.stream().map((entity) -> {
            entity.setSpuId(spuId);
            return entity;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }
}
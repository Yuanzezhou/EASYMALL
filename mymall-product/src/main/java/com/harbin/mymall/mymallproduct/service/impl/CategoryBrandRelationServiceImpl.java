package com.harbin.mymall.mymallproduct.service.impl;

import com.harbin.mymall.mymallproduct.dao.BrandDao;
import com.harbin.mymall.mymallproduct.entity.BrandEntity;
import com.harbin.mymall.mymallproduct.service.BrandService;
import com.harbin.mymall.mymallproduct.vo.BrandVo;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.CategoryBrandRelationDao;
import com.harbin.mymall.mymallproduct.entity.CategoryBrandRelationEntity;
import com.harbin.mymall.mymallproduct.service.CategoryBrandRelationService;

import javax.annotation.Resource;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Resource
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(Long catId) {
        List<CategoryBrandRelationEntity> categoryBrandRelationEntities = this.getBaseMapper().selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> brandVos = categoryBrandRelationEntities.stream().map((item) -> {
            BrandEntity brandEntity = brandDao.selectById(item.getBrandId());
            return brandEntity;
        }).collect(Collectors.toList());
        return brandVos;
    }
}
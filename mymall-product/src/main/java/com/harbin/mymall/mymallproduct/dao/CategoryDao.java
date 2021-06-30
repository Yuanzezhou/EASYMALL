package com.harbin.mymall.mymallproduct.dao;

import com.harbin.mymall.mymallproduct.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}

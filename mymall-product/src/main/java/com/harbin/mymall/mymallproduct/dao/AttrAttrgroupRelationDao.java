package com.harbin.mymall.mymallproduct.dao;

import com.harbin.mymall.mymallproduct.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:07
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    //@Param是mybatis用来给mapper.xml传递参数的。
    void deleteBatchRelation(@Param("entites") List<AttrAttrgroupRelationEntity> entities);
}

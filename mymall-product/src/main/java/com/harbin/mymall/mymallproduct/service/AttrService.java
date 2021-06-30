package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.AttrAttrgroupRelationEntity;
import com.harbin.mymall.mymallproduct.entity.AttrEntity;
import com.harbin.mymall.mymallproduct.vo.AttrRespVo;
import com.harbin.mymall.mymallproduct.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:07
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryPage(Map<String, Object> params, Long catelogId,String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrGroupId);

    void deleteRelation(AttrAttrgroupRelationEntity[] entities);

    PageUtils getnNoRelationAttr(Map<String, Object> params, Long attrGroupId);

    List<Long> selectSearchAttrs(List<Long> attrIds);

}


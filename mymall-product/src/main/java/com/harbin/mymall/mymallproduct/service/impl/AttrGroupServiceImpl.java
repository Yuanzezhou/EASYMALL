package com.harbin.mymall.mymallproduct.service.impl;

import com.harbin.mymall.mymallproduct.dao.AttrDao;
import com.harbin.mymall.mymallproduct.entity.AttrEntity;
import com.harbin.mymall.mymallproduct.service.AttrService;
import com.harbin.mymall.mymallproduct.vo.AttrGroupWithAttrsVo;
import com.harbin.mymall.mymallproduct.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.AttrGroupDao;
import com.harbin.mymall.mymallproduct.entity.AttrGroupEntity;
import com.harbin.mymall.mymallproduct.service.AttrGroupService;
import org.w3c.dom.Attr;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Resource
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        if(catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        }else{
            String key = (String)params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
            if(!StringUtils.isEmpty(key)){
                wrapper.and((obj)-> {
                    obj.eq("attr_group_id", key).or().like("attr_group_name", key);
                });
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            System.out.println("哈哈！");
            return new PageUtils(page);
        }
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrs(Long catelogId) {
        //1、查询分组信息；
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        //2、根据分组查询属性；
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVoList = attrGroupEntities.stream().map((item) -> {
            AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
            List<AttrEntity> relationAttr = attrService.getRelationAttr(item.getAttrGroupId());
            BeanUtils.copyProperties(item,vo);
            vo.setAttrs(relationAttr);
            return vo;
        }).collect(Collectors.toList());
        return attrGroupWithAttrsVoList;
    }

    @Override
    public List<SpuItemAttrGroupVo> getAttrGroupWithAttrsBySpuId(Long spuId, Long catalogId) {
        List<SpuItemAttrGroupVo> spuItemAttrGroupVos = this.baseMapper.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
        return spuItemAttrGroupVos;

    }

}
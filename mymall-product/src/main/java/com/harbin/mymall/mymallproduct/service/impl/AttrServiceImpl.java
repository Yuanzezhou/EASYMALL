package com.harbin.mymall.mymallproduct.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.harbin.common.constant.ProductConstant;
import com.harbin.mymall.mymallproduct.dao.AttrAttrgroupRelationDao;
import com.harbin.mymall.mymallproduct.dao.AttrGroupDao;
import com.harbin.mymall.mymallproduct.dao.CategoryDao;
import com.harbin.mymall.mymallproduct.entity.AttrAttrgroupRelationEntity;
import com.harbin.mymall.mymallproduct.entity.AttrGroupEntity;
import com.harbin.mymall.mymallproduct.entity.CategoryEntity;
import com.harbin.mymall.mymallproduct.service.AttrAttrgroupRelationService;
import com.harbin.mymall.mymallproduct.service.CategoryService;
import com.harbin.mymall.mymallproduct.vo.AttrRespVo;
import com.harbin.mymall.mymallproduct.vo.AttrVo;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.AttrDao;
import com.harbin.mymall.mymallproduct.entity.AttrEntity;
import com.harbin.mymall.mymallproduct.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Resource
    AttrAttrgroupRelationDao relationDao;

    @Resource
    CategoryDao categoryDao;

    @Resource
    AttrGroupDao attrGroupDao;

    @Resource
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
//        attrEntity.setAttrName(attr.getAttrName());
        BeanUtils.copyProperties(attr,attrEntity);
        //1、保存基本数据
        this.save(attrEntity);
        //2、保存关联关系
        Long attrGroupId = attr.getAttrGroupId();
        if(attrGroupId != null && attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(attrAttrgroupRelationEntity);
        }
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId,String attrType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_type","base".equalsIgnoreCase(attrType)?ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode():ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode());
        if(catelogId != 0){
            wrapper.eq("catelog_id", catelogId);
        }
        String key = (String)params.get("key");
        if(!StringUtils.isEmpty(key)) {
            wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }

        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params),
                wrapper);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((entity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(entity, attrRespVo);

            //分类名
            CategoryEntity categoryEntity = categoryDao.selectById(entity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }

            //分组名(销售属性没有分组)
            if("base".equalsIgnoreCase(attrType)){
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = relationDao.selectOne(
                        new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", entity.getAttrId()));
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                if(attrGroupEntity != null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        PageUtils pageUtils = new PageUtils(page);
        pageUtils.setList(respVos);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties( attrEntity,attrRespVo);
        //1、设置分类
        Long[] catelogPath = categoryService.findCatelogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if(categoryEntity != null){
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        //2、设置分组
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = relationDao.selectById(attrId);
            if(attrAttrgroupRelationEntity != null){
                attrRespVo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                if(attrGroupEntity != null){
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }
        return attrRespVo;
    }

    @Transactional  //事务注解
    @Override
    public void updateAttr(AttrVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        this.updateById(attrEntity);
        //1、修改分组关联
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            Long attrGroupId = attr.getAttrGroupId();
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
            Integer attr_id = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            if(attr_id > 0){
                //做修改操作
                relationDao.update(attrAttrgroupRelationEntity,new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            }else{
                //原先没有，做增加操作
                relationDao.insert(attrAttrgroupRelationEntity);
            }
        }
    }

    /**
     * 根据分组id查询所有attrId.
     * @param attrGroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> attrs = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> attrIds = attrs.stream().map((attrAttrgroupentity) -> {
            return attrAttrgroupentity.getAttrId();
        }).collect(Collectors.toList());

        Collection<AttrEntity> attrEntities = this.listByIds(attrIds);
        return (List<AttrEntity>) attrEntities;
    }

    @Override
    public void deleteRelation(AttrAttrgroupRelationEntity[] entities) {
        List<AttrAttrgroupRelationEntity> entitiesList = Arrays.asList(entities);
        relationDao.deleteBatchRelation(entitiesList);
    }

    @Override
    public PageUtils getnNoRelationAttr(Map<String, Object> params, Long attrGroupId) {
        //1、当前分组只能关联自己所属分类里面的所有属性
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //2、当前分组只能关联别的分组没有引用的属性
        //2.1)当前分类下的其他分组
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> groupIds = attrGroupEntities.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //2.2）这些分组关联的属性
        List<AttrAttrgroupRelationEntity> attr_group_id = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().in("attr_group_id", groupIds));
        List<Long> attrIds = attr_group_id.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //2.3）从当前分类的所有属性中移除这些属性
        String key = (String) params.get("key");
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>().eq("catelog_id", catelogId);
        if(!CollectionUtils.isEmpty(attrIds)){
            queryWrapper.notIn("attr_id", attrIds);
        }
        if(! StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }
        this.baseMapper.selectList(queryWrapper);
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {

        return baseMapper.selectSearchAttrs(attrIds);
    }
}
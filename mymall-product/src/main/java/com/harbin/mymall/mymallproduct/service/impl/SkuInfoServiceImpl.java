package com.harbin.mymall.mymallproduct.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.harbin.common.utils.R;
import com.harbin.mymall.mymallproduct.entity.SkuImagesEntity;
import com.harbin.mymall.mymallproduct.entity.SpuInfoDescEntity;
import com.harbin.mymall.mymallproduct.feign.SeckillFeignService;
import com.harbin.mymall.mymallproduct.service.*;
import com.harbin.mymall.mymallproduct.vo.SeckillInfoVo;
import com.harbin.mymall.mymallproduct.vo.SkuItemSaleAttrVo;
import com.harbin.mymall.mymallproduct.vo.SkuItemVo;
import com.harbin.mymall.mymallproduct.vo.SpuItemAttrGroupVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.SkuInfoDao;
import com.harbin.mymall.mymallproduct.entity.SkuInfoEntity;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    ThreadPoolExecutor executor;

    @Autowired
    SeckillFeignService seckillFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            queryWrapper.and((wrapper)->{
                wrapper.eq("sku_id",key).or().like("sku_name",key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){

            queryWrapper.eq("catalog_id",catelogId);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(catelogId)){
            queryWrapper.eq("brand_id",brandId);
        }

        String min = (String) params.get("min");
        if(!StringUtils.isEmpty(min)){
            queryWrapper.ge("price",min);
        }

        String max = (String) params.get("max");

        if(!StringUtils.isEmpty(max)  ){
            try{
                BigDecimal bigDecimal = new BigDecimal(max);

                if(bigDecimal.compareTo(new BigDecimal("0"))==1){
                    queryWrapper.le("price",max);
                }
            }catch (Exception e){

            }
        }
        IPage<SkuInfoEntity> page = this.page(new Query<SkuInfoEntity>().getPage(params), queryWrapper);
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> skuInfoEntities = this.baseMapper.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return skuInfoEntities;
    }

    /**
     * ??????????????????????????????????????????
     * @param skuId
     * @return
     */
//    @Override
//    public SkuItemVo getItem(Long skuId) {
//        SkuItemVo skuItemVo = new SkuItemVo();
//        //1???sku??????????????????    pms_sku_info
//        SkuInfoEntity info = getById(skuId);
//        skuItemVo.setInfo(info);
//        Long catalogId = info.getCatalogId();
//        Long spuId = info.getSpuId();
//
//        //2???sku???????????????      pms_sku_images
//        List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
//        skuItemVo.setImages(images);
//
//        //3?????????spu?????????????????????
//        List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(spuId);
//        skuItemVo.setSaleAttr(saleAttrVos);
//
//        //4?????????spu????????? pms_spu_info_desc
//        SpuInfoDescEntity desc = spuInfoDescService.getById(spuId);
//        skuItemVo.setDesc(desc);
//
//        //5?????????spu?????????????????????
//        List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
//        skuItemVo.setGroupAttrs(attrGroupVos);
//        return skuItemVo;
//    }

    /**
     * ??????????????????????????????
     * @param skuId
     * @return
     */
    @Override
    public SkuItemVo getItem(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //1???sku??????????????????    pms_sku_info
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, executor);

        //???????????????????????????????????????(1)???????????????info??????
        CompletableFuture<Void> saleFuture = infoFuture.thenAcceptAsync((res) -> {
            //3?????????spu?????????????????????
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVo.setSaleAttr(saleAttrVos);
        }, executor);

        CompletableFuture<Void> spuDescFuture = infoFuture.thenAcceptAsync((res) -> {
            //4?????????spu????????? pms_spu_info_desc
            SpuInfoDescEntity desc = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(desc);
        }, executor);

        CompletableFuture<Void> baseAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //5?????????spu?????????????????????
            List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setGroupAttrs(attrGroupVos);
        }, executor);


        //?????????????????????????????????????????????????????????????????????
        CompletableFuture<Void> imgFuture = CompletableFuture.runAsync(() -> {
            //2???sku???????????????      pms_sku_images
            List<SkuImagesEntity> images = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        }, executor);

        CompletableFuture<Void> seckillFuture = CompletableFuture.runAsync(() -> {
            // 3???????????????sku????????????????????????
            R r = seckillFeignService.getSkuSecKillInfo(skuId);
            if (0 == r.getCode()) {
                SeckillInfoVo skillInfo = r.getData(new TypeReference<SeckillInfoVo>() {
                });
                skuItemVo.setSeckillInfoVo(skillInfo);
            }
        }, executor);

        //????????????????????????????????????????????????
        CompletableFuture<Void> allOf = CompletableFuture.allOf(infoFuture, saleFuture, spuDescFuture, imgFuture, baseAttrFuture);
        //???????????????????????????????????????
        allOf.get();
        return skuItemVo;
    }
}
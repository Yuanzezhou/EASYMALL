package com.harbin.mymall.mymallproduct.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.harbin.common.constant.ProductConstant;
import com.harbin.common.to.SkuReductionTo;
import com.harbin.common.to.SpuBoundTo;
import com.harbin.common.to.es.SkuEsModel;
import com.harbin.common.utils.R;
import com.harbin.mymall.mymallproduct.entity.*;
import com.harbin.mymall.mymallproduct.feign.ElasticSearchFeignService;
import com.harbin.mymall.mymallproduct.feign.SpuFeignService;
import com.harbin.mymall.mymallproduct.feign.WareFeignService;
import com.harbin.mymall.mymallproduct.service.*;
import com.harbin.mymall.mymallproduct.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallproduct.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService attrValueService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SpuInfoService spuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private SpuFeignService couponFeignService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private ElasticSearchFeignService esFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo spuSaveVo) {

        //1、保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        //2、保存spu的描述图片 pms_spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        List<String> descript = spuSaveVo.getDecript();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", descript));
        spuInfoDescService.saveSpuInfoDescs(spuInfoDescEntity);

        //3、保存spu的图片集 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);

        //4、保存spu的规格参数；pms_product_attr_value;
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            AttrEntity id = attrService.getById(attr.getAttrId());
            valueEntity.setAttrName(id.getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(spuInfoEntity.getId());

            return valueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);

        //5、保存spu的积分信息 sms_spu_bounds
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        couponFeignService.saveSpuBounds(spuBoundTo);

        //6、保存当前spu对应的所有sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        if(!CollectionUtils.isEmpty(skus)){
            skus.forEach(item->{
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                //    private String skuName;
                //    private BigDecimal price;
                //    private String skuTitle;
                //    private String skuSubtitle;
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item,skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                //6.1）、sku的基本信息；pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity->{
                    //返回true就是需要，false就是剔除
                    return !StringUtils.isEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //6.2）、sku的图片信息；pms_sku_image
                skuImagesService.saveBatch(imagesEntities);
                //TODO 没有图片路径的无需保存

                //6.3）、sku的销售属性信息：pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);

                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.4）、sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if(skuReductionTo.getFullCount() >0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1){
                    R r1 = couponFeignService.saveSkuReduction(skuReductionTo);
                    if(r1.getCode() != 0){
                        log.error("远程保存sku优惠信息失败");
                    }
                }
            });
        }
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();

        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        // status=1 and (id=1 or spu_name like xxx),这就是and()的作用
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",status);
        }

        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }

        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    @Override
    public void up(Long spuId) {
        List<SkuInfoEntity> skusBySpuId = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuIds = skusBySpuId.stream().map(sku -> {
            return sku.getSkuId();
        }).collect(Collectors.toList());

        //TODO 4、查询当前sku的所有可以被用来检索的规格属性
        List<ProductAttrValueEntity> baseAttrs = attrValueService.listForSpu(spuId);
        List<Long> attrIds = baseAttrs.stream().map(attr -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<Long> searchAttrIds =attrService.selectSearchAttrs(attrIds);

        HashSet<Long> attrSet = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrsList = baseAttrs.stream().filter(attr -> {
            return attrSet.contains(attr.getAttrId());
        }).map(attr -> {
            SkuEsModel.Attrs attrs = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(attr, attrs);
            return attrs;
        }).collect(Collectors.toList());

        Map<Long, Boolean> stockMap = null;
        //TODO 1、发送远程调用到库存系统批量查询是否有库存
        try{
            R skusHasStock = wareFeignService.getHasStock(skuIds);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>(){};
            stockMap = skusHasStock.getData(typeReference).stream().collect(Collectors.toMap(item -> item.getSkuId(), SkuHasStockVo::getHasStock));
        }catch (Exception e){
            log.error("库存服务远程调用出现异常，原因是{}",e);
        }

        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);
        //这个map是不会变化的，即final effectively
        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> upProducts = skuInfoEntities.stream().map((entity) -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(entity, skuEsModel);
            //skuPrice、skuImg
            skuEsModel.setSkuPrice(entity.getPrice());
            skuEsModel.setSkuImg(entity.getSkuDefaultImg());
            //hasStock、hotScore
            if(finalStockMap == null){
                skuEsModel.setHasStock(true);
            }else{
                skuEsModel.setHasStock(finalStockMap.get(skuEsModel.getSkuId()));
            }
            //TODO 2、热度评分；
            skuEsModel.setHotScore(0L);
            //TODO 3、查询品牌和分类的名字
            BrandEntity brand = brandService.getById(skuEsModel.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());
            CategoryEntity category = categoryService.getById(skuEsModel.getCatalogId());
            skuEsModel.setCatalogName(category.getName());

            skuEsModel.setAttrs(attrsList);
            return skuEsModel;
        }).collect(Collectors.toList());

        //TODO 5、将数据发送给es进行保存：调用mymall-search服务
        R r = esFeignService.productStatusUp(upProducts);
        if(r.getCode() == 0){
            //远程调用成功
            //TODO 6、修改spu的状态为上架；
            baseMapper.updateSpuStatus(spuId, ProductConstant.StatusEnum.SPU_UP.getCode());
        }else{
            //远程调用失败
            //TODO 7、重复调用？即接口幂等性、重试机制问题
        }
    }

    @Override
    public SpuInfoEntity getSpuInfoBySkuId(Long skuId) {
        SkuInfoEntity byId = skuInfoService.getById(skuId);
        Long spuId = byId.getSpuId();
        SpuInfoEntity spuInfoEntity = getById(spuId);

        return spuInfoEntity;
    }

//    @Override
//    public void updateSpuStatus(Long spuId, int code) {
///*       访问数据库次数太多了。
//        SpuInfoEntity spuInfoEntity = this.getById(spuId);
//        spuInfoEntity.setPublishStatus(code);
//        spuInfoEntity.setUpdateTime(new Date());
//        this.updateById(spuInfoEntity);*/
//    }

    private void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.getBaseMapper().insert(spuInfoEntity);
    }
}
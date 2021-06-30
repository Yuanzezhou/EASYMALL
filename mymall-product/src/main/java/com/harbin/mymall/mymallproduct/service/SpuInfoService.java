package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.SpuInfoEntity;
import com.harbin.mymall.mymallproduct.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-02-05 15:45:23
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo spuSaveVo);

    PageUtils queryPageByCondition(Map<String, Object> params);

    void up(Long spuId);

    SpuInfoEntity getSpuInfoBySkuId(Long skuId);
}


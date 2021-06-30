package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-02-05 15:45:23
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long id, List<String> images);
}


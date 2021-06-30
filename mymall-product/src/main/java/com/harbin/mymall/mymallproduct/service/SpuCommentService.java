package com.harbin.mymall.mymallproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallproduct.entity.SpuCommentEntity;

import java.util.Map;

/**
 * 商品评价
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-02-05 15:45:23
 */
public interface SpuCommentService extends IService<SpuCommentEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


package com.harbin.mymall.mymallorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallorder.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:12
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


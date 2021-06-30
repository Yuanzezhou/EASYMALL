package com.harbin.mymall.mymallware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:54
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}


package com.harbin.mymall.mymallorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallorder.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:13
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


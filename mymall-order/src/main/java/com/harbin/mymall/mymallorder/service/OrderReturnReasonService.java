package com.harbin.mymall.mymallorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallorder.entity.OrderReturnReasonEntity;

import java.util.Map;

/**
 * 退货原因
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:12
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


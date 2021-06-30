package com.harbin.mymall.mymallware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.to.mq.OrderTo;
import com.harbin.common.to.mq.StockLockedTo;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallware.entity.WareSkuEntity;
import com.harbin.mymall.mymallware.vo.SkuHasStockVo;
import com.harbin.mymall.mymallware.vo.WareSkuLockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:53
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStocks(Long skuId, Integer skuNum, Long wareId);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    Boolean orderLockStock(WareSkuLockVo vo);

    void unLock(StockLockedTo to);

    void unLockStockForOrder(OrderTo to);
}


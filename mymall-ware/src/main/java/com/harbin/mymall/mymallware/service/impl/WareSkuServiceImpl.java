package com.harbin.mymall.mymallware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.harbin.common.constant.OrderConstant;
import com.harbin.common.to.mq.OrderTo;
import com.harbin.common.to.mq.StockDetailTo;
import com.harbin.common.to.mq.StockLockedTo;
import com.harbin.common.utils.R;
import com.harbin.mymall.mymallware.constant.WareLockStatusEnum;
import com.harbin.mymall.mymallware.entity.WareOrderTaskDetailEntity;
import com.harbin.mymall.mymallware.entity.WareOrderTaskEntity;
import com.harbin.mymall.mymallware.exception.NoStockException;
import com.harbin.mymall.mymallware.feign.OrderFeignService;
import com.harbin.mymall.mymallware.feign.ProductFeignService;
import com.harbin.mymall.mymallware.service.WareOrderTaskDetailService;
import com.harbin.mymall.mymallware.service.WareOrderTaskService;
import com.harbin.mymall.mymallware.vo.OrderItemVo;
import com.harbin.mymall.mymallware.vo.SkuHasStockVo;
import com.harbin.mymall.mymallware.vo.WareSkuLockVo;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallware.dao.WareSkuDao;
import com.harbin.mymall.mymallware.entity.WareSkuEntity;
import com.harbin.mymall.mymallware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {


    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderFeignService orderFeignService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {

        /**
         * skuId: 1
         * wareId: 2
         */
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }

        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }

        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void addStocks(Long skuId, Integer skuNum, Long wareId) {

        //1、判断如果还没有这个库存记录就要新增

        WareSkuEntity wareSkuEntity = this.getOne(new QueryWrapper<WareSkuEntity>()
                .eq("sku_id", skuId)
                .eq("ware_id", wareId));

        if(wareSkuEntity == null){
            //新增
            wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStockLocked(0);
            //如果远程查询失败，整个事务不需要进行回滚。
            //1、catch异常，addStocks的异常不要向上抛
            //TODO 还有什么其他方法使得异常不要回滚呢？==>高级部分
            try {
                R info = productFeignService.info(skuId);
                if(info.getCode() == 0){
                    Map<String,Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                    String skuName = (String) skuInfo.get("skuName");
                    wareSkuEntity.setSkuName(skuName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.save(wareSkuEntity);

        }else{
            //修改库存
            wareSkuEntity.setStock(wareSkuEntity.getStock()+skuNum);
            this.updateById(wareSkuEntity);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> skuHasStockVos = skuIds.stream().map(skuId -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            Long count = baseMapper.getSkuStock(skuId);
            skuHasStockVo.setSkuId(skuId);
            skuHasStockVo.setHasStock(count == null ? false:true);
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return skuHasStockVos;
    }

    /**
     * 为某个订单锁定库存
     * (rollbackFor = NoStockException.class)
     * 默认只要都是运行异常都会回滚
     * @param vo
     * @return
     */
    @Transactional
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {

        //因为可能出现订单回滚后，库存锁定不回滚的情况，但订单已经回滚，得不到库存锁定信息，因此要有库存工作单
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        taskEntity.setCreateTime(new Date());
        wareOrderTaskService.save(taskEntity);

        // 1、按照下单的收货地址，找到一个就近仓库，锁定库存

        // 1、找到每个商品在哪个仓库都有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪个仓库有库存
            List<Long> wareIds = this.baseMapper.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());
        // 2、锁定库存
        for (SkuWareHasStock hasStock : collect) {
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if (wareIds == null || wareIds.size() == 0){
                //没有任何库存有这个商品的库存
                throw new NoStockException(skuId);
            }
            for (Long wareId : wareIds) {
                //成功返回1；否则就是0
                Long count = this.baseMapper.lockSkuStock(skuId,wareId,hasStock.getNum());
                if (count == 1){
                    //锁定成功，保存工作单详情
                    WareOrderTaskDetailEntity detailEntity = new WareOrderTaskDetailEntity(null,skuId,"",hasStock.getNum(),taskEntity.getId(),wareId,1);
                    wareOrderTaskDetailService.save(detailEntity);
                    //发送库存锁定消息至延迟队列
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(taskEntity.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(detailEntity,detailTo);
                    lockedTo.setDetailTo(detailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",lockedTo);
                    skuStocked = true;
                    break;
                    //当仓库锁失败，重试下一个仓库
                }
            }
            if (skuStocked == false){
                //当前商品所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }
        // 3、肯定全部都是锁定成功的
        return true;
    }

    @Override
    public void unLock(StockLockedTo to) {
        StockDetailTo detail = to.getDetailTo();
        Long skuId = detail.getSkuId();
        Long detailToId = detail.getId();
        /**
         * 解锁：
         * 1、查询数据库关于这个订单的锁定信息；
         * 若有：则解锁:
         *         再看订单情况：
         *              1、没有这个订单，必须解锁；
         *              2、有这个订单。就不直接解锁库存：
         *                      再根据订单状态：已取消，解锁库存。未取消，不解锁库存
         *
         * 若没有：则库存已经回滚，无需解锁
         */
        WareOrderTaskDetailEntity detailEntity = wareOrderTaskDetailService.getById(detailToId);
        if(detailEntity != null){
            //查询到锁定信息，需要解锁
            Long id = to.getId();   //工作单id
            WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getById(id);
            String orderSn = wareOrderTaskEntity.getOrderSn();
            R r = orderFeignService.getOrderStatus(orderSn);
            if(r.getCode() == 0){
                //订单状态数据返回成功
                OrderTo order = r.getData(new TypeReference<OrderTo>() {});
                if(order == null || order.getStatus() == OrderConstant.OrderStatusEnum.CANCLED.getCode()){
                    //订单已经被取消了，才能解锁库存
                    //再次判断库存是否已经被解锁了；
                    unLockStock(skuId,detailEntity.getWareId(),detailEntity.getSkuNum(),detailEntity.getId(),detailEntity.getTaskId());
                }
            }else{
                throw new RuntimeException("远程调用订单服务失败");
            }
        }
    }



    /**
     * 防止订单服务卡顿，导致订单状态一直改变不了，库存消息优先到期，查订单状态新建状态，什么都不做就走了
     * 导致卡顿的订单，永远不能解锁库存
     * @param to
     */
    @Transactional
    @Override
    public void unLockStockForOrder(OrderTo to) {
        String orderSn = to.getOrderSn();
        //查一下最新的库存解锁状态，防止重复解锁库存
        R r = orderFeignService.getOrderStatus(orderSn);
        WareOrderTaskEntity task = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = task.getId();
        //按照工作单找到所有 没有解锁的库存，进行解锁
        List<WareOrderTaskDetailEntity> entities = wareOrderTaskDetailService.list(new QueryWrapper<WareOrderTaskDetailEntity>().eq("task_id", id).eq("lock_status", 1));
        for (WareOrderTaskDetailEntity entity : entities) {
            unLockStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum(),entity.getId(),id);
        }
    }


    private void unLockStock(Long skuId, Long wareId, Integer skuNum, Long detailId,Long taskId) {
        Integer status = wareOrderTaskDetailService.getStatusByTaskId(taskId);
        if(status == WareLockStatusEnum.LOCKED.getCode()){
            wareOrderTaskDetailService.updateLockStatus(WareLockStatusEnum.UNLOCKED.getCode(),taskId);
            this.baseMapper.unLockStock(skuId,wareId,skuNum,detailId);
        }
    }

    @Data
    class SkuWareHasStock{

        private Long skuId;

        private Integer num;

        private List<Long> wareId;
    }

}
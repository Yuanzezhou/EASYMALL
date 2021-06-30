package com.harbin.mymall.mymallware.service.impl;

import com.harbin.common.constant.WareConstant;
import com.harbin.mymall.mymallware.entity.PurchaseDetailEntity;
import com.harbin.mymall.mymallware.service.PurchaseDetailService;
import com.harbin.mymall.mymallware.service.WareSkuService;
import com.harbin.mymall.mymallware.vo.MergeVo;
import com.harbin.mymall.mymallware.vo.PurchaseDoneVo;
import com.harbin.mymall.mymallware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.Query;

import com.harbin.mymall.mymallware.dao.PurchaseDao;
import com.harbin.mymall.mymallware.entity.PurchaseEntity;
import com.harbin.mymall.mymallware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {

        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );

        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId == null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatus.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        }

        if(this.getById(purchaseId).getStatus() == WareConstant.PurchaseStatus.CREATED.getCode() ||
                this.getById(purchaseId).getStatus() == WareConstant.PurchaseStatus.ASSIGNED.getCode()){
            List<Long> items = mergeVo.getItems();
            Long finalPurchaseId = purchaseId;
            List<PurchaseDetailEntity> collect = items.stream().map((item) -> {
                //没必要多一次查询
                //PurchaseDetailEntity entity = purchaseDetailService.getById(item);
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(item);
                purchaseDetailEntity.setPurchaseId(finalPurchaseId);
                purchaseDetailEntity.setStatus(WareConstant.PurchaseStatus.ASSIGNED.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());

            purchaseDetailService.updateBatchById(collect);

            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setId(purchaseId);
            this.updateById(purchaseEntity);
        }
    }

    @Transactional
    @Override
    public void received(List<Long> ids) {
        //1、确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatus.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatus.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            item.setStatus(WareConstant.PurchaseStatus.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //2、改变采购单的状态
        this.updateBatchById(collect);

        //3、改变采购项的状态
        collect.forEach((item)->{
            List<PurchaseDetailEntity> entities = purchaseDetailService.listDetailByPurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatus.BUYING.getCode());
                return entity1;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntities);
        });
    }

    @Transactional
    @Override
    public void finishPurchase(PurchaseDoneVo purchaseDoneVo) {
        Long purchaseId = purchaseDoneVo.getId();
        //1、设置采购项的状态
        List<PurchaseItemDoneVo> items = purchaseDoneVo.getItems();
        boolean flag = true;
        ArrayList<PurchaseDetailEntity> updates = new ArrayList<>();
        for(PurchaseItemDoneVo item:items){
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(item.getItemId());
            if(item.getStatus() == WareConstant.PurchaseDetailStatus.HASERROR.getCode()){
                flag = false;
            }else{
                //3、成功采购的采购项入库
                PurchaseDetailEntity entity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStocks(entity.getSkuId(),entity.getSkuNum(),entity.getWareId());
            }
            detailEntity.setStatus(item.getStatus());
            updates.add(detailEntity);
        }

        purchaseDetailService.updateBatchById(updates);
        //2、设置采购单的状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatus.FINISH.getCode():WareConstant.PurchaseStatus.HASERROR.getCode());
        this.updateById(purchaseEntity);
    }
}
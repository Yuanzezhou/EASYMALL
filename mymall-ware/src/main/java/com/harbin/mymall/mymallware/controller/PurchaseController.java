package com.harbin.mymall.mymallware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.harbin.mymall.mymallware.vo.MergeVo;
import com.harbin.mymall.mymallware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallware.entity.PurchaseEntity;
import com.harbin.mymall.mymallware.service.PurchaseService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 采购信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:54
 */
@RestController
@RequestMapping("mymallware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * http://localhost:88/api/mymallware/purchase/unreceive/list
     * 合并采购单时列出未接收的采购单
     */
    @GetMapping("/unreceive/list")
    public R listUnreceive(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceivePurchase(params);
        return R.ok().put("page", page);
    }

    /**
     * http://localhost:88/api/mymallware/purchase/merge
     * 自动创建采购单
     */

    @PostMapping("/merge")
    public R mergePurchase(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     *领取采购单
     * @param ids
     * @return
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){

        purchaseService.received(ids);

        return R.ok();
    }

    /**
     * /mymallware/purchase/done
     * 完成采购
     */

    @PostMapping("/done")
    public R finishPurchase(@RequestBody PurchaseDoneVo purchaseDoneVo){

        purchaseService.finishPurchase(purchaseDoneVo);

        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

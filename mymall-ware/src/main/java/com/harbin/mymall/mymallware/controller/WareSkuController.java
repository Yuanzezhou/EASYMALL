package com.harbin.mymall.mymallware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.harbin.common.exception.BizCodeEnum;
import com.harbin.mymall.mymallware.exception.NoStockException;
import com.harbin.mymall.mymallware.vo.SkuHasStockVo;
import com.harbin.mymall.mymallware.vo.WareSkuLockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallware.entity.WareSkuEntity;
import com.harbin.mymall.mymallware.service.WareSkuService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 商品库存
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:53
 */
@RestController
@RequestMapping("mymallware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    @PostMapping("/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo){
        try{
            Boolean stock = wareSkuService.orderLockStock(vo);
            return R.ok();
        }catch (NoStockException e){
            return R.error(BizCodeEnum.NO_STOCK_EXCEPTION.getCode(),BizCodeEnum.NO_STOCK_EXCEPTION.getMsg());
        }
    }

    /**
     * 查询是否有库存
     */

    @PostMapping("/hasStock")
    public R getSkuHasStock(@RequestBody List<Long> skuIds){
        List<SkuHasStockVo> vos = wareSkuService.getSkuHasStock(skuIds);

        return R.ok().setData(vos);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallware:waresku:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallware:waresku:info")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallware:waresku:save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallware:waresku:update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallware:waresku:delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

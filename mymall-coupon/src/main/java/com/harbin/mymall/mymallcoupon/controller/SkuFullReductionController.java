package com.harbin.mymall.mymallcoupon.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.harbin.common.to.SkuReductionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallcoupon.entity.SkuFullReductionEntity;
import com.harbin.mymall.mymallcoupon.service.SkuFullReductionService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 商品满减信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 23:10:43
 */
@RestController
@RequestMapping("mymallcoupon/skufullreduction")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;


    /**
     * 保存sku的满减和折扣信息
     */

    @PostMapping("/saveinfo")
    public R saveSkuInfo(@RequestBody SkuReductionTo skuReductionTo){
        skuFullReductionService.saveSkuInfo(skuReductionTo);
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallcoupon:skufullreduction:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallcoupon:skufullreduction:info")
    public R info(@PathVariable("id") Long id){
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallcoupon:skufullreduction:save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallcoupon:skufullreduction:update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallcoupon:skufullreduction:delete")
    public R delete(@RequestBody Long[] ids){
		skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

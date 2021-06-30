package com.harbin.mymall.mymallproduct.app;

import java.util.Arrays;
import java.util.Map;

import com.harbin.mymall.mymallproduct.entity.SpuInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallproduct.entity.SkuInfoEntity;
import com.harbin.mymall.mymallproduct.service.SkuInfoService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * sku信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
@RestController
@RequestMapping("mymallproduct/skuinfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;


    @GetMapping("/{skuId}/price")
    public R getPrice(@PathVariable("skuId") Long skuId){
        SkuInfoEntity byId = skuInfoService.getById(skuId);
        return R.ok().setData(byId.getPrice().toString());
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallproduct:skuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{skuId}")
    //@RequiresPermissions("mymallproduct:skuinfo:info")
    public R info(@PathVariable("skuId") Long skuId){
		SkuInfoEntity skuInfo = skuInfoService.getById(skuId);

        return R.ok().put("skuInfo", skuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallproduct:skuinfo:save")
    public R save(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.save(skuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallproduct:skuinfo:update")
    public R update(@RequestBody SkuInfoEntity skuInfo){
		skuInfoService.updateById(skuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallproduct:skuinfo:delete")
    public R delete(@RequestBody Long[] skuIds){
		skuInfoService.removeByIds(Arrays.asList(skuIds));

        return R.ok();
    }

}

package com.harbin.mymall.mymallcoupon.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallcoupon.entity.SpuBoundsEntity;
import com.harbin.mymall.mymallcoupon.service.SpuBoundsService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 商品spu积分设置
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 23:10:43
 */
@RestController
@RequestMapping("mymallcoupon/spubounds")
public class SpuBoundsController {
    @Autowired
    private SpuBoundsService spuBoundsService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallcoupon:spubounds:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuBoundsService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallcoupon:spubounds:info")
    public R info(@PathVariable("id") Long id){
		SpuBoundsEntity spuBounds = spuBoundsService.getById(id);

        return R.ok().put("spuBounds", spuBounds);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    //@RequiresPermissions("mymallcoupon:spubounds:save")
    public R save(@RequestBody SpuBoundsEntity spuBounds){
        boolean save = spuBoundsService.save(spuBounds);
        System.out.println(save);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallcoupon:spubounds:update")
    public R update(@RequestBody SpuBoundsEntity spuBounds){
		spuBoundsService.updateById(spuBounds);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallcoupon:spubounds:delete")
    public R delete(@RequestBody Long[] ids){
		spuBoundsService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

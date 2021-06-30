package com.harbin.mymall.mymallproduct.app;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.harbin.mymall.mymallproduct.vo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallproduct.entity.SpuInfoEntity;
import com.harbin.mymall.mymallproduct.service.SpuInfoService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;


/**
 * spu信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-02-05 15:45:23
 */
@RestController
@RequestMapping("mymallproduct/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    @GetMapping("skuId/{id}")
    public R getSpuInfoBySkuId(@PathVariable("id") Long skuId){
        SpuInfoEntity entity = spuInfoService.getSpuInfoBySkuId(skuId);
        return R.ok().setData(entity);
    }


    /**
     * http://localhost:88/api/mymallproduct/spuinfo/{spuId}/up
     * 商品上架共功能
     */


    @PostMapping("/{spuId}/up")
    public R spuUp(@PathVariable("spuId") Long spuId){
        spuInfoService.up(spuId);
        return R.ok();
    }


    /**
     * http://localhost:88/api/mymallproduct/spuinfo/save
     * 发布商品时保存商品信息
     */

    @PostMapping("/save")
    public R saveSpu(@RequestBody SpuSaveVo spuSaveVo){
        spuInfoService.saveSpuInfo(spuSaveVo);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallproduct:spuinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = spuInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallproduct:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallproduct:spuinfo:save")
    public R save(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.save(spuInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallproduct:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallproduct:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

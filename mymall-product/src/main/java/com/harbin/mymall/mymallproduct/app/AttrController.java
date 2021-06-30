package com.harbin.mymall.mymallproduct.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.harbin.mymall.mymallproduct.entity.ProductAttrValueEntity;
import com.harbin.mymall.mymallproduct.service.ProductAttrValueService;
import com.harbin.mymall.mymallproduct.vo.AttrRespVo;
import com.harbin.mymall.mymallproduct.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallproduct.service.AttrService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 商品属性
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:07
 */
@RestController
@RequestMapping("mymallproduct/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    /**
     * http://localhost:88/api/mymallproduct/attr/base/listforspu/{spuId}
     * 规格维护
     */

     @GetMapping("/base/listforspu/{spuId}")
     public R listForSpu(@PathVariable Long spuId){
     List<ProductAttrValueEntity> entities =  productAttrValueService.listForSpu(spuId);
     return R.ok().put("data", entities);
     }
     /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallproduct:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }

    //mymallproduct/attr/${type}/list/${this.catId}
//    @RequestMapping("/base/list/{catelogId}")
    @RequestMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId,
                          @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryPage(params, catelogId,attrType);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("mymallproduct:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);

        AttrRespVo attrInfo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallproduct:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    //@RequiresPermissions("mymallproduct:attr:update")
    public R update(@RequestBody AttrVo attr){
//		attrService.updateById(attr);
        attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * http://localhost:88/api/mymallproduct/attr/update/15
     * 批量修改规格维护中修改的基本参数
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@RequestBody List<ProductAttrValueEntity> entities,@PathVariable("spuId") Long spuId){
        productAttrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallproduct:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }
}

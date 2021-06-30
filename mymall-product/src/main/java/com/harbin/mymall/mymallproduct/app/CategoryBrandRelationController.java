package com.harbin.mymall.mymallproduct.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.harbin.mymall.mymallproduct.entity.BrandEntity;
import com.harbin.mymall.mymallproduct.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallproduct.entity.CategoryBrandRelationEntity;
import com.harbin.mymall.mymallproduct.service.CategoryBrandRelationService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
@RestController
@RequestMapping("mymallproduct/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 根据分类Id选择品牌
     * /brands/list
     *
     * 1、Controller:处理请求，接受和校验数据
     * 2、Service:接受controller传来的数据，进行业务逻辑；
     * 3、Controller接受Service处理完的数据，封装页面指定的VO。
     */
    @GetMapping("/brands/list")
    public R brandsList(@RequestParam(value="catId",required = true) Long catId){
        List<BrandEntity> brandList =  categoryBrandRelationService.getBrandsByCatId(catId);

        List<BrandVo> brandVos = brandList.stream().map((brandEntity) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(brandEntity.getBrandId());
            brandVo.setBrandName(brandEntity.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data",brandVos);
    }

    /**
     * /mymallproduct/categorybrandrelation/catelog/list
     * 品牌关联分类
     */
    public R brandCatelogRelation(@RequestParam(value="brandId",required = true) Long brandId){
        //TODO
//        categoryBrandRelationService.getCatelogId()
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallproduct:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallproduct:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallproduct:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.save(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallproduct:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallproduct:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

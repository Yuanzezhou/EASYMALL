package com.harbin.mymall.mymallproduct.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harbin.mymall.mymallproduct.entity.CategoryEntity;
import com.harbin.mymall.mymallproduct.service.CategoryService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;

/**
 * 商品三级分类
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:06
 */
@RestController
@RequestMapping("mymallproduct/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list/tree")
    public R listTree(){

        //获得所有数据
        List<CategoryEntity> categoryEntities = categoryService.listWithTree();
        return R.ok().put("data",categoryEntities);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallproduct:category:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("mymallproduct:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallproduct:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallproduct:category:update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 删除
     *
     * @RequestBody这个注解的请求一定要是post请求。
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallproduct:category:delete")
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeByIds(Arrays.asList(catIds));
		categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }
}

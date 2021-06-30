package com.harbin.mymall.mymallproduct.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.harbin.mymall.mymallproduct.entity.AttrAttrgroupRelationEntity;
import com.harbin.mymall.mymallproduct.entity.AttrEntity;
import com.harbin.mymall.mymallproduct.service.AttrAttrgroupRelationService;
import com.harbin.mymall.mymallproduct.service.AttrService;
import com.harbin.mymall.mymallproduct.service.CategoryService;
import com.harbin.mymall.mymallproduct.vo.AttrGroupRelationVo;
import com.harbin.mymall.mymallproduct.vo.AttrGroupWithAttrsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallproduct.entity.AttrGroupEntity;
import com.harbin.mymall.mymallproduct.service.AttrGroupService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 属性分组
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:07
 */
@RestController
@RequestMapping("mymallproduct/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService relationService;

    /**
     * http://localhost:88/api/mymallproduct/attrgroup/{catelogId}/withattr
     * 获取某一分类id对应的属性分组和属性
     */

    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttr(@PathVariable("catelogId") Long catelogId){
        //1、查出当前分类下所有的属性分组；
        //2、查出每个分组的所有属性；
        List<AttrGroupWithAttrsVo> vos = attrGroupService.getAttrGroupWithAttrs(catelogId);
        return R.ok().put("data",vos);
    }

    /**
     * 属性分组关联
     */
        //mymallproduct/attrgroup/7/attr/relation
    @RequestMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> entities =  attrService.getRelationAttr(attrGroupId);
        return R.ok().put("data",entities);
    }

    @RequestMapping("/{attrGroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrGroupId") Long attrGroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page =  attrService.getnNoRelationAttr(params,attrGroupId);
        return R.ok().put("page",page);
    }
    /**
     * 删除关联
     */
    //PostMapping的请求发送Json参数时，给参数加上RequestBody注解。
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrAttrgroupRelationEntity[] entities){
        attrService.deleteRelation(entities);
        return R.ok();
    }

    /**
     * 增加关联
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("mymallproduct:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params,catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("mymallproduct:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);

        Long catelogId = attrGroup.getCatelogId();
        Long[] catelogPath = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallproduct:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallproduct:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallproduct:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}

package com.harbin.mymall.mymallmember.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harbin.mymall.mymallmember.entity.GrowthChangeHistoryEntity;
import com.harbin.mymall.mymallmember.service.GrowthChangeHistoryService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 成长值变化历史记录
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:12:51
 */
@RestController
@RequestMapping("mymallmember/growthchangehistory")
public class GrowthChangeHistoryController {
    @Autowired
    private GrowthChangeHistoryService growthChangeHistoryService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallmember:growthchangehistory:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = growthChangeHistoryService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallmember:growthchangehistory:info")
    public R info(@PathVariable("id") Long id){
		GrowthChangeHistoryEntity growthChangeHistory = growthChangeHistoryService.getById(id);

        return R.ok().put("growthChangeHistory", growthChangeHistory);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallmember:growthchangehistory:save")
    public R save(@RequestBody GrowthChangeHistoryEntity growthChangeHistory){
		growthChangeHistoryService.save(growthChangeHistory);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallmember:growthchangehistory:update")
    public R update(@RequestBody GrowthChangeHistoryEntity growthChangeHistory){
		growthChangeHistoryService.updateById(growthChangeHistory);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallmember:growthchangehistory:delete")
    public R delete(@RequestBody Long[] ids){
		growthChangeHistoryService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

package com.harbin.mymall.mymallware.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.harbin.mymall.mymallware.vo.FareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallware.entity.WareInfoEntity;
import com.harbin.mymall.mymallware.service.WareInfoService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 仓库信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:54
 */
@RestController
@RequestMapping("mymallware/wareinfo")
public class WareInfoController {
    @Autowired
    private WareInfoService wareInfoService;

    @GetMapping("/fare")
    public R getFare(@RequestParam("addrId") Long addrId){
        FareVo fare = wareInfoService.getFare(addrId);
        return R.ok().setData(fare);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallware:wareinfo:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareInfoService.queryPageByCondition(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallware:wareinfo:info")
    public R info(@PathVariable("id") Long id){
		WareInfoEntity wareInfo = wareInfoService.getById(id);

        return R.ok().put("wareInfo", wareInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallware:wareinfo:save")
    public R save(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.save(wareInfo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallware:wareinfo:update")
    public R update(@RequestBody WareInfoEntity wareInfo){
		wareInfoService.updateById(wareInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallware:wareinfo:delete")
    public R delete(@RequestBody Long[] ids){
		wareInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

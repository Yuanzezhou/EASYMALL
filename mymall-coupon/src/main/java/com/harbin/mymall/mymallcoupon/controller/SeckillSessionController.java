package com.harbin.mymall.mymallcoupon.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.harbin.mymall.mymallcoupon.service.SeckillSkuRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallcoupon.entity.SeckillSessionEntity;
import com.harbin.mymall.mymallcoupon.service.SeckillSessionService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 秒杀活动场次
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 23:10:44
 */
@RestController
@RequestMapping("mymallcoupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    @Autowired
    SeckillSkuRelationService seckillSkuRelationService;

    /**
     * 秒杀服务上架调用的请求
     * @return
     */
    @GetMapping("/lasts3DaySession")
    public R getLasts3DaySession(){
        List<SeckillSessionEntity> session = seckillSessionService.getLasts3DaySession();
        return R.ok().setData(session);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallcoupon:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallcoupon:seckillsession:info")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallcoupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.save(seckillSession);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallcoupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallcoupon:seckillsession:delete")
    public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

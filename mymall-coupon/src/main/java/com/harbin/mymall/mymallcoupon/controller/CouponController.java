package com.harbin.mymall.mymallcoupon.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.harbin.mymall.mymallcoupon.entity.CouponEntity;
import com.harbin.mymall.mymallcoupon.service.CouponService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 优惠券信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 23:10:45
 */
@RefreshScope
@RestController
@RequestMapping("mymallcoupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @Value("${coupon.user.name}")
    private String name;

    @Value("${coupon.user.age}")
    private Integer age;

    @RequestMapping("/testconfig")
    public R testconfig(){
        return R.ok().put("name", name).put("age", age);
    }

    @RequestMapping("/member/list")
    public  R menbercoupons(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("满1000减100！");
        return R.ok().put("coupons",Arrays.asList(couponEntity));
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("mymallcoupon:coupon:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallcoupon:coupon:info")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallcoupon:coupon:save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallcoupon:coupon:update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallcoupon:coupon:delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

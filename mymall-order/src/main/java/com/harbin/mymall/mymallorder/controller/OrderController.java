package com.harbin.mymall.mymallorder.controller;

import java.util.Arrays;
import java.util.Map;

//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.harbin.mymall.mymallorder.entity.OrderEntity;
import com.harbin.mymall.mymallorder.service.OrderService;
import com.harbin.common.utils.PageUtils;
import com.harbin.common.utils.R;



/**
 * 订单
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:09:13
 */
@RestController
@RequestMapping("mymallorder/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 根据orderSn查询订单状态
     */
    @GetMapping("/status/{orderSn}")
    public R getOrderStatus(@PathVariable("orderSn") String orderSn){
        OrderEntity orderEntity = orderService.getOrderByOrderSn(orderSn);
        return R.ok().setData(orderEntity);
    }

    /**
     * 列表
     */

    @RequestMapping("/list")
    //@RequiresPermissions("mymallorder:order:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 查询当前登录用户的订单
     */

    @PostMapping("/listWithItems")
    public R listWithItems(@RequestBody Map<String,Object> params){
        PageUtils page = orderService.queryPageWithItems(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("mymallorder:order:info")
    public R info(@PathVariable("id") Long id){
		OrderEntity order = orderService.getById(id);

        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("mymallorder:order:save")
    public R save(@RequestBody OrderEntity order){
		orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("mymallorder:order:update")
    public R update(@RequestBody OrderEntity order){
		orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("mymallorder:order:delete")
    public R delete(@RequestBody Long[] ids){
		orderService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}

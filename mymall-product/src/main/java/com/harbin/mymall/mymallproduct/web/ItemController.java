package com.harbin.mymall.mymallproduct.web;

import com.harbin.mymall.mymallproduct.service.SkuInfoService;
import com.harbin.mymall.mymallproduct.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * @author Yuanzz
 * @creat 2021-03-03-14:43
 */
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    /**
     * 展示当前sku详情页面
     * @return
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = skuInfoService.getItem(skuId);
        model.addAttribute("item", skuItemVo);
        return "item";
    }
}

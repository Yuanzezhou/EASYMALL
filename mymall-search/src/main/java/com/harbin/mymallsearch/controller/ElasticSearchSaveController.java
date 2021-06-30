package com.harbin.mymallsearch.controller;

import com.harbin.common.exception.BizCodeEnum;
import com.harbin.common.to.es.SkuEsModel;
import com.harbin.common.utils.R;
import com.harbin.mymallsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-20-21:05
 */
@RequestMapping("/mymallsearch/save")
@RestController
@Slf4j
public class ElasticSearchSaveController {

    @Autowired
    ProductSaveService productSaveService;
    //上架商品
    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels){
        boolean ifSuccess = false;
        try{
            ifSuccess = productSaveService.productStatusUp(skuEsModels);
        }
        catch (Exception e){
            log.error("ES商品上架错误：{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }

        if(!ifSuccess){
            return R.ok();
        }else{
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}

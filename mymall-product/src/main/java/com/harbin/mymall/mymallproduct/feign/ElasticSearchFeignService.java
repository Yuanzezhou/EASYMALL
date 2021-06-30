package com.harbin.mymall.mymallproduct.feign;

import com.harbin.common.to.es.SkuEsModel;
import com.harbin.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-20-21:55
 */

@FeignClient("mymall-search")
public interface ElasticSearchFeignService {

    @PostMapping("mymallsearch/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}

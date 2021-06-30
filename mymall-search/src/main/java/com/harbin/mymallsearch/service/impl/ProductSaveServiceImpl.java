package com.harbin.mymallsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.harbin.common.constant.ProductConstant;
import com.harbin.common.to.es.SkuEsModel;
import com.harbin.mymallsearch.constant.EsConstant;
import com.harbin.mymallsearch.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.harbin.mymallsearch.config.ElasticSearchConfig.COMMON_OPTIONS;

/**
 * @author Yuanzz
 * @creat 2021-02-20-21:15
 */

@Service("productSaveService")
@Slf4j
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    RestHighLevelClient client;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        //保存到es
        //1、给es中建立索引。product,建立好映射关系---kibana操作

        //2、给es中保存这些数据；
        //BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for(SkuEsModel skuEsModel:skuEsModels){
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String s = JSON.toJSONString(skuEsModel);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = client.bulk(bulkRequest, COMMON_OPTIONS);
        boolean b = bulk.hasFailures();

        List<String> collect = Arrays.stream(bulk.getItems()).map(item -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.info("商品上架完成：{},返回数据：{}",collect,bulk.toString());
        return b;
    }
}

package com.harbin.mymallsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yuanzz
 * @creat 2021-02-10-21:10
 */

/**
 * RestHighLevelClient操作步骤:
 * 1、导入依赖
 * 2、编写配置
 * 3、
 */

@Configuration
public class ElasticSearchConfig {

    //RequestOptions
    public static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
//        builder.addHeader("Authorization", "Bearer " + TOKEN);
//        builder.setHttpAsyncResponseConsumerFactory(
//                new HttpAsyncResponseConsumerFactory
//                        .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient esRestClient(){
        //aliyun服务器
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                        new HttpHost("59.110.167.53", 9200, "http")));

        //虚拟机centos7
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.112.100", 9200, "http")));

        return client;
    }
}

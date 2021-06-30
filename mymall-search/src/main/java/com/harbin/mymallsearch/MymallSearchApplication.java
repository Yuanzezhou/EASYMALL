package com.harbin.mymallsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)		//因为common中有数据库的相关依赖，若不配置相应configuration就会报错。
@EnableDiscoveryClient
public class MymallSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallSearchApplication.class, args);
	}

}

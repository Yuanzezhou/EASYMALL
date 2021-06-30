package com.harbin.mymallauthserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;


@EnableRedisHttpSession
@EnableFeignClients("com.harbin.mymallauthserver.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MymallAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallAuthServerApplication.class, args);
	}

}

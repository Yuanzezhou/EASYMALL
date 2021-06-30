package com.harbin.mymall.mymallorder;

import com.alibaba.cloud.seata.GlobalTransactionAutoConfiguration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableAspectJAutoProxy(exposeProxy = true)
@EnableRedisHttpSession
@EnableRabbit
@SpringBootApplication(exclude = GlobalTransactionAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients("com.harbin.mymall.mymallorder.feign")
public class MymallOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallOrderApplication.class, args);
	}

}

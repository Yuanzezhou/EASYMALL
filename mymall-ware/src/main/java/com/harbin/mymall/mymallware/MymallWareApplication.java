package com.harbin.mymall.mymallware;

import com.alibaba.cloud.seata.GlobalTransactionAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableRabbit
@SpringBootApplication(exclude = GlobalTransactionAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.harbin.mymall.mymallware.feign")
public class MymallWareApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallWareApplication.class, args);
	}

}

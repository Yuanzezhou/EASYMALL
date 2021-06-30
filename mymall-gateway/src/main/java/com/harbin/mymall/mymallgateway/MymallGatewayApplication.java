package com.harbin.mymall.mymallgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import javax.sql.DataSource;


@EnableDiscoveryClient	//服务发现
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MymallGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallGatewayApplication.class, args);
	}
}

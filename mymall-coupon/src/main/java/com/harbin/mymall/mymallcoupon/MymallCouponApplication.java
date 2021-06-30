package com.harbin.mymall.mymallcoupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MymallCouponApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallCouponApplication.class, args);
	}

}

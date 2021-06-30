package com.harbin.mymallthirdservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MymallthirdserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallthirdserviceApplication.class, args);
	}

}

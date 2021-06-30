package com.harbin.mymall.mymallware;

import com.harbin.common.utils.R;
import com.harbin.mymall.mymallware.feign.OrderFeignService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.transform.Source;

@SpringBootTest
class MymallWareApplicationTests {

	@Autowired
	OrderFeignService orderFeignService;

	@Test
	void contextLoads() {
		R orderStatus = orderFeignService.getOrderStatus("123242222222222222222");
		System.out.println(orderStatus);
	}
}

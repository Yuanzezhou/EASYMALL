package com.harbin.mymall.mymallorder;

import com.harbin.mymall.mymallorder.entity.OrderEntity;
import com.harbin.mymall.mymallorder.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class MymallOrderApplicationTests {

	@Autowired
	AmqpAdmin amqpAdmin;

	@Autowired
	RabbitTemplate rabbitTemplate;

	@Autowired
	OrderService orderService;

	@Test
	public void testOrderService(){
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setOrderSn("12545454664512222222222");
		orderEntity.setCreateTime(new Date());
		orderService.save(orderEntity);
	}

	@Test
	void contextLoads() {
		System.out.println("啦啦啦啦");
	}

	@Test
	public void testExchange(){
		DirectExchange exchange = new DirectExchange("order", true, false);
		amqpAdmin.declareExchange(exchange);
		log.info("穿建完成！");
	}

	@Test
	public void testQueue(){
		Queue queue = new Queue("hello",true,false,false);
		String s = amqpAdmin.declareQueue(queue);
		log.info("创建完成");
	}

	@Test
	public void createBinding(){
		Binding binding = new Binding("hello", Binding.DestinationType.QUEUE,"easymall","hello.java",null);
		amqpAdmin.declareBinding(binding);
		log.info("Binding[{}]创建成功","hello-java-binding");
	}

	@Test
	public void testSendMsg(){
		String msg = "hello world!";
		rabbitTemplate.convertAndSend("easymall","hello.java",msg);
		log.info("发送完成！");
	}

	@Test
	public void testSendObject(){
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(1L);
		orderEntity.setCreateTime(new Date());
		orderEntity.setNote("哈哈");
		//默认是以jdk序列化机制进行序列化
		//在RabbitConfig中配置json序列化器
		rabbitTemplate.convertAndSend("easymall","hello.java",orderEntity);
		log.info("发送完成！");
	}
}

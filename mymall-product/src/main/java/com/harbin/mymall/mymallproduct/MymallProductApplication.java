package com.harbin.mymall.mymallproduct;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
@MapperScan("com.harbin.mymall.mymallproduct.dao")
@EnableDiscoveryClient
@EnableFeignClients("com.harbin.mymall.mymallproduct.feign")
@SpringBootApplication
@EnableCaching
public class MymallProductApplication {

/*
*一、整合Mybatis—Plus
*
* 		1）导入依赖
* 		2）配置-参照官网：
* 			1、配置数据源
* 				导入数据库驱动+配置yaml信息
* 			2、配置mybatis-plus:
* 				使用mapperScan注解；
* 				告诉mybatis-pluss mapper映射文件的位置，在yaml中配置
*
*二、逻辑删除：
* 1、配置全局的逻辑删除规则(省略);
* 2、配置逻辑删除的组件Bean(省略);
* 3、给Bean加上逻辑删除注释@TableLogic;
*
* 三、JSR0303后台数据校验
* 1、在Entity类中配置校验规则（对应的属性上标注@NotNUll、@Email等）;
* 2、在Controller的参数上标注（@Valid）使其生效；
* 3、分组校验
* 		1）@NotBlank(message="品牌名不能为空哦！",groups={AddGroup.class,UpdateGroup.class})
* 		2)@Validated({AddGroup})指定生效的组；
* 		3）默认没有指定分组的校验注解@NotBlank，在分组校验情况下不生效，只会在@Validated情况下（不指定分组）生效。
*
* 四、集中异常处理
* 1）编写异常处理类，使用@ControllerAdvice。
* 2）使用@ExceptionHandler标注方法可以处理的异常。
*
* 五、JSR303高阶功能：分组表单校验
* 利用groups以及定义的标志接口：此处未展示，具体见文档。
*
* 六、模板引擎
*
* 七、整合Redis
* (1）引入“spring-boot-starter-data-redis”
*（2）简单配置Redis的host等信息
*（3）使用SpringBoot自动配置好的"StringRedisTemplate"来操作redis。
*
* 八、整合Redisson作为分布式锁等功能框架
* 1）导入依赖
* 2）在配置类中添加@Bean组件
*
* 九、整合spring-cache简化缓存开发
* 1）引入依赖
* 2）编写配置
* 	①自动配置：CacheAutoConfiguration，会导入RedisCacheConfiguration
				自动配置了缓存管理器RedisCacheManager；
* 	②配置redis作为缓存：修改“application.properties”文件，指定使用redis作为缓存，
						spring.cache.type=redis；
*
**/

	public static void main(String[] args) {
		SpringApplication.run(MymallProductApplication.class, args);
	}
}

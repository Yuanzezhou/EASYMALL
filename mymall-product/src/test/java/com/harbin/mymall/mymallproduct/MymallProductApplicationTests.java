package com.harbin.mymall.mymallproduct;


import com.harbin.mymall.mymallproduct.dao.AttrGroupDao;
import com.harbin.mymall.mymallproduct.dao.SkuSaleAttrValueDao;
import com.harbin.mymall.mymallproduct.entity.BrandEntity;
import com.harbin.mymall.mymallproduct.service.BrandService;
import com.harbin.mymall.mymallproduct.service.CategoryService;
import com.harbin.mymall.mymallproduct.vo.SkuItemSaleAttrVo;
import com.harbin.mymall.mymallproduct.vo.SpuItemAttrGroupVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.xml.ws.Endpoint;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MymallProductApplicationTests {

	@Autowired
	BrandService brandService;

	@Autowired
	private CategoryService categoryService;
//	@Resource
//	OSS ossClient;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RedissonClient redissonClient;

	@Autowired
	AttrGroupDao attrGroupDao;

	@Autowired
	SkuSaleAttrValueDao skuSaleAttrValueDao;

	@Test
	public void testAttrGroupDao(){
		List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupDao.getAttrGroupWithAttrsBySpuId(15l, 225l);
		System.out.println(attrGroupWithAttrsBySpuId);
	}

	@Test
	public void testSkuSaleAttrValueDao(){
		List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(15l);
		System.out.println(saleAttrsBySpuId);
	}

	@Test
	public void testRedisson(){
		System.out.println(redissonClient);
	}


	@Test
	public void testRedis(){
		ValueOperations<String, String> ops = redisTemplate.opsForValue();
		ops.set("hello", "world"+ UUID.randomUUID().toString());
		String hello = ops.get("hello");
		System.out.println(hello);
	}


	@Test
	public void contextLoads() {
		BrandEntity brandEntity = new BrandEntity();
		brandEntity.setName("哈哈哈");
		brandEntity.setBrandId(1L);
		brandEntity.setLogo("1234567");
		brandService.save(brandEntity);
		System.out.println(brandService.getById(1));
	}


//	@Test
//	public void test() throws FileNotFoundException {
//		// Endpoint以杭州为例，其它Region请按实际情况填写。
////		String endpoint = "oss-cn-beijing.aliyuncs.com";
////// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
////		String accessKeyId = "LTAI4FzvCL76TNUueg6hv27b";
////		String accessKeySecret = "6sCJtLaYGrxlJ2E1zyGKWa4A6Nclce";
//		String bucketName = "mymall123";
//// <yourObjectName>上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
//		String objectName = "C:\\Users\\ASUS\\Pictures\\yzz.jpg";
//
////// 创建OSSClient实例。
////		OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//// 上传文件到指定的存储空间（bucketName）并将其保存为指定的文件名称（objectName）。
//		FileInputStream fileInputStream = new FileInputStream(objectName);
//
//		ossClient.putObject(bucketName,"yzz2.jpg", fileInputStream);
//// 关闭OSSClient。
//		ossClient.shutdown();
//		System.out.println("上传成功。。");
//	}

	@Test
	public void testFindCategoryPath(){
		Long[] catelogPath = categoryService.findCatelogPath(224L);
		log.info("路径:{}", Arrays.asList(catelogPath));
	}
}

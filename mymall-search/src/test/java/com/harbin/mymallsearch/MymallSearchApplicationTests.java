package com.harbin.mymallsearch;

import com.alibaba.fastjson.JSON;
import com.harbin.mymallsearch.config.ElasticSearchConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

@SpringBootTest
class MymallSearchApplicationTests {

	@Resource
	private RestHighLevelClient client;
	@Test
	void contextLoads() {
		System.out.println(client);
	}

	@Test
	void index() throws IOException {
		IndexRequest indexRequest = new IndexRequest("users");
		indexRequest.id("1");
//		indexRequest.source("userName","zhangsan","age","18","gender","男");

		User user = new User("zhangsan",18,"男");
		String jsonString = JSON.toJSONString(user);
		indexRequest.source(jsonString, XContentType.JSON);
		IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);
		System.out.println(index);
	}

	@Data
	@AllArgsConstructor
	class User{
		private String userName;
		private Integer age;
		private String gender;
	}

	@Test
	public void searchData() throws IOException {
	//1. 创建检索请求
		SearchRequest searchRequest = new SearchRequest();

	//1.1）指定索引
		searchRequest.indices("bank");
	//1.2）构造检索条件
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(QueryBuilders.matchQuery("address","Mill"));

	//1.2.1)按照年龄分布进行聚合
		TermsAggregationBuilder ageAgg= AggregationBuilders.terms("ageAgg").field("age").size(10);
		sourceBuilder.aggregation(ageAgg);

	//1.2.2)计算平均年龄
		AvgAggregationBuilder ageAvg = AggregationBuilders.avg("ageAvg").field("age");
		sourceBuilder.aggregation(ageAvg);
	//1.2.3)计算平均薪资
		AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
		sourceBuilder.aggregation(balanceAvg);

		System.out.println("检索条件："+sourceBuilder);
		searchRequest.source(sourceBuilder);
	//2. 执行检索
		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
		System.out.println("检索结果："+searchResponse);

	//3. 分析结果
		//将结果封装成一个map
		//Map map = JSON.parseObject(searchResponse.toString(), Map.class);

		//直接获取所有结果:
		SearchHits hits = searchResponse.getHits();
		SearchHit[] searchHits = hits.getHits();
		for (SearchHit searchHit : searchHits) {
			String sourceAsString = searchHit.getSourceAsString();
			Account account = JSON.parseObject(sourceAsString, Account.class);
			System.out.println(account);
		}

	//4. 获取聚合信息
		Aggregations aggregations = searchResponse.getAggregations();

		Terms ageAgg1 = aggregations.get("ageAgg");

		for (Terms.Bucket bucket : ageAgg1.getBuckets()) {
			String keyAsString = bucket.getKeyAsString();
			System.out.println("年龄："+keyAsString+" ==> "+bucket.getDocCount());
		}
		Avg ageAvg1 = aggregations.get("ageAvg");
		System.out.println("平均年龄："+ageAvg1.getValue());

		Avg balanceAvg1 = aggregations.get("balanceAvg");
		System.out.println("平均薪资："+balanceAvg1.getValue());
	}

	@Data
	public static class Account {

		private int account_number;
		private int balance;
		private String firstname;
		private String lastname;
		private int age;
		private String gender;
		private String address;
		private String employer;
		private String email;
		private String city;
		private String state;
	}
}

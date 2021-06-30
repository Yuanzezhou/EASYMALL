package com.harbin.mymallsearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.harbin.common.to.es.SkuEsModel;
import com.harbin.mymallsearch.constant.EsConstant;
import com.harbin.mymallsearch.service.MallSearchService;
import com.harbin.mymallsearch.vo.SearchParam;
import com.harbin.mymallsearch.vo.SearchResult;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.harbin.mymallsearch.config.ElasticSearchConfig.COMMON_OPTIONS;

/**
 * @author Yuanzz
 * @creat 2021-03-02-16:51
 */

@Service
public class MallSearchServiceImpl implements MallSearchService {

    @Autowired
    RestHighLevelClient client;

    @Override
    public SearchResult search(SearchParam param) {

        //动态构建出查询需要的DSL语句
        SearchResult searchResult = null;
        //1、准备检索请求
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            //2、执行检索请求
            SearchResponse search = client.search(searchRequest, COMMON_OPTIONS);

            //3、分析响应数据封装成我们需要的格式；
            searchResult = buildSearchResponse(search, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    private SearchRequest buildSearchRequest(SearchParam param){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();//构建DSL语句的
        /**
         * 过滤（按照属性、分类、品牌、价格区间、库存）
         */
        //1、构建bool-query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(boolQuery);
        //1.1 must-模糊匹配、
        if (!StringUtils.isEmpty(param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }
        //1.2.1 filter-按照三级分类id查询
        if (null != param.getCatalog3Id()){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
        }
        //1.2.2 filter-按照品牌id查询
        if (null != param.getBrandId() && param.getBrandId().size()>0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId",param.getBrandId()));
        }
        //1.2.3 filter-按照是否有库存进行查询
        if (null != param.getHasStock() ) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        //1.2.4 filter-按照区间进行查询  1_500/_500/500_
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            String[] prices = param.getSkuPrice().split("_");
            if (prices.length == 1) {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQueryBuilder.lte(Integer.parseInt(prices[0]));
                }else {
                    rangeQueryBuilder.gte(Integer.parseInt(prices[0]));
                }
            } else if (prices.length == 2) {
                //_6000会截取成["","6000"]
                if (!prices[0].isEmpty()) {
                    rangeQueryBuilder.gte(Integer.parseInt(prices[0]));
                }
                rangeQueryBuilder.lte(Integer.parseInt(prices[1]));
            }
            boolQuery.filter(rangeQueryBuilder);
        }
        //1.2.5 filter-按照属性进行查询
        List<String> attrs = param.getAttrs();
        if (null != attrs && attrs.size() > 0) {
            //attrs=1_5寸:8寸&2_16G:8G
            attrs.forEach(attr->{
                BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
                String[] attrSplit = attr.split("_");
                queryBuilder.must(QueryBuilders.termQuery("attrs.attrId", attrSplit[0]));//检索的属性的id
                String[] attrValues = attrSplit[1].split(":");
                queryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));//检索的属性的值
                //每一个必须都得生成一个nested查询
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", queryBuilder, ScoreMode.None);
                boolQuery.filter(nestedQueryBuilder);
            });
        }
        //把以前所有的条件都拿来进行封装
        sourceBuilder.query(boolQuery);
        /**
         * 排序，分页，高亮，
         */
        //2.1 排序  eg:sort=saleCount_desc/asc
        if (!StringUtils.isEmpty(param.getSort())) {
            String[] sortSplit = param.getSort().split("_");
            sourceBuilder.sort(sortSplit[0], sortSplit[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
        }

        //2.2、分页
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        //2.3 高亮highlight
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /**
         * 聚合分析
         */
        //5. 聚合
        //5.1 按照品牌聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg").field("brandId").size(50);
        //品牌聚合的子聚合
        TermsAggregationBuilder brand_name_agg = AggregationBuilders.terms("brand_name_agg").field("brandName").size(1);
        TermsAggregationBuilder brand_img_agg = AggregationBuilders.terms("brand_img_agg").field("brandImg");
        brand_agg.subAggregation(brand_name_agg);
        brand_agg.subAggregation(brand_img_agg);
        sourceBuilder.aggregation(brand_agg);

        //5.2 按照catalog聚合
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        TermsAggregationBuilder catalog_name_agg = AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1);
        catalog_agg.subAggregation(catalog_name_agg);
        sourceBuilder.aggregation(catalog_agg);

        //5.3 按照attrs聚合
        NestedAggregationBuilder nestedAggregationBuilder = new NestedAggregationBuilder("attr_agg", "attrs");
        //按照attrId聚合
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //按照attrId聚合之后再按照attrName和attrValue聚合
        TermsAggregationBuilder attr_name_agg = AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1);
        TermsAggregationBuilder attr_value_agg = AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50);
        attr_id_agg.subAggregation(attr_name_agg);
        attr_id_agg.subAggregation(attr_value_agg);

        nestedAggregationBuilder.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(nestedAggregationBuilder);

        String s = sourceBuilder.toString();
        System.out.println("构建的DSL"+s);

        SearchRequest request = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return request;
    }

    private SearchResult buildSearchResponse(SearchResponse response, SearchParam param){
        SearchResult result = new SearchResult();
        //1、返回的所有查询到的商品
        SearchHits hits = response.getHits();
        List<SkuEsModel> esModels = new ArrayList<>();
        if (null != hits.getHits() && hits.getHits().length>0){
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if (!StringUtils.isEmpty(param.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    esModel.setSkuTitle(skuTitle.fragments()[0].string());
                }
                esModels.add(esModel);
            }
        }
        result.setProducts(esModels);
        //2、当前所有商品涉及到的所有属性
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //1、得到属性的id;
            long attrId = bucket.getKeyAsNumber().longValue();
            //2、得到属性的名字
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            //3、得到属性的所有值
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> {
                String keyAsString = item.getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());
            attrVo.setAttrId(attrId);
            attrVo.setAttrName(attrName);
            attrVo.setAttrValue(attrValues);
            attrVos.add(attrVo);
        }

        result.setAttrs(attrVos);
        //3、当前所有品牌涉及到的所有属性
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //1、得到品牌的id
            long brandId = bucket.getKeyAsNumber().longValue();
            //2、得到品牌的名
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            //3、得到品牌的图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        //4、当前商品所涉及的分类信息
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");

        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            //得到分类id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));

            //得到分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);
        //===========以上从聚合信息获取到=============
        //5、分页信息-页码
        result.setPageNum(param.getPageNum());
        //6、分页信息-总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //7、分页信息-总页码-计算
        int totalPages = total%EsConstant.PRODUCT_PAGESIZE == 0 ?(int) total/EsConstant.PRODUCT_PAGESIZE:((int)total/EsConstant.PRODUCT_PAGESIZE+1);
        result.setTotalPages(totalPages);
        return result;
    }
}

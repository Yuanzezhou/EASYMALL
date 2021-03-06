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

        //??????????????????????????????DSL??????
        SearchResult searchResult = null;
        //1?????????????????????
        SearchRequest searchRequest = buildSearchRequest(param);
        try {
            //2?????????????????????
            SearchResponse search = client.search(searchRequest, COMMON_OPTIONS);

            //3??????????????????????????????????????????????????????
            searchResult = buildSearchResponse(search, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResult;
    }

    private SearchRequest buildSearchRequest(SearchParam param){
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();//??????DSL?????????
        /**
         * ??????????????????????????????????????????????????????????????????
         */
        //1?????????bool-query
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        sourceBuilder.query(boolQuery);
        //1.1 must-???????????????
        if (!StringUtils.isEmpty(param.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("skuTitle",param.getKeyword()));
        }
        //1.2.1 filter-??????????????????id??????
        if (null != param.getCatalog3Id()){
            boolQuery.filter(QueryBuilders.termQuery("catalogId",param.getCatalog3Id()));
        }
        //1.2.2 filter-????????????id??????
        if (null != param.getBrandId() && param.getBrandId().size()>0) {
            boolQuery.filter(QueryBuilders.termsQuery("brandId",param.getBrandId()));
        }
        //1.2.3 filter-?????????????????????????????????
        if (null != param.getHasStock() ) {
            boolQuery.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        //1.2.4 filter-????????????????????????  1_500/_500/500_
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
                //_6000????????????["","6000"]
                if (!prices[0].isEmpty()) {
                    rangeQueryBuilder.gte(Integer.parseInt(prices[0]));
                }
                rangeQueryBuilder.lte(Integer.parseInt(prices[1]));
            }
            boolQuery.filter(rangeQueryBuilder);
        }
        //1.2.5 filter-????????????????????????
        List<String> attrs = param.getAttrs();
        if (null != attrs && attrs.size() > 0) {
            //attrs=1_5???:8???&2_16G:8G
            attrs.forEach(attr->{
                BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
                String[] attrSplit = attr.split("_");
                queryBuilder.must(QueryBuilders.termQuery("attrs.attrId", attrSplit[0]));//??????????????????id
                String[] attrValues = attrSplit[1].split(":");
                queryBuilder.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));//?????????????????????
                //?????????????????????????????????nested??????
                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", queryBuilder, ScoreMode.None);
                boolQuery.filter(nestedQueryBuilder);
            });
        }
        //?????????????????????????????????????????????
        sourceBuilder.query(boolQuery);
        /**
         * ???????????????????????????
         */
        //2.1 ??????  eg:sort=saleCount_desc/asc
        if (!StringUtils.isEmpty(param.getSort())) {
            String[] sortSplit = param.getSort().split("_");
            sourceBuilder.sort(sortSplit[0], sortSplit[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
        }

        //2.2?????????
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGESIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGESIZE);

        //2.3 ??????highlight
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }

        /**
         * ????????????
         */
        //5. ??????
        //5.1 ??????????????????
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg").field("brandId").size(50);
        //????????????????????????
        TermsAggregationBuilder brand_name_agg = AggregationBuilders.terms("brand_name_agg").field("brandName").size(1);
        TermsAggregationBuilder brand_img_agg = AggregationBuilders.terms("brand_img_agg").field("brandImg");
        brand_agg.subAggregation(brand_name_agg);
        brand_agg.subAggregation(brand_img_agg);
        sourceBuilder.aggregation(brand_agg);

        //5.2 ??????catalog??????
        TermsAggregationBuilder catalog_agg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        TermsAggregationBuilder catalog_name_agg = AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1);
        catalog_agg.subAggregation(catalog_name_agg);
        sourceBuilder.aggregation(catalog_agg);

        //5.3 ??????attrs??????
        NestedAggregationBuilder nestedAggregationBuilder = new NestedAggregationBuilder("attr_agg", "attrs");
        //??????attrId??????
        TermsAggregationBuilder attr_id_agg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //??????attrId?????????????????????attrName???attrValue??????
        TermsAggregationBuilder attr_name_agg = AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1);
        TermsAggregationBuilder attr_value_agg = AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50);
        attr_id_agg.subAggregation(attr_name_agg);
        attr_id_agg.subAggregation(attr_value_agg);

        nestedAggregationBuilder.subAggregation(attr_id_agg);
        sourceBuilder.aggregation(nestedAggregationBuilder);

        String s = sourceBuilder.toString();
        System.out.println("?????????DSL"+s);

        SearchRequest request = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);
        return request;
    }

    private SearchResult buildSearchResponse(SearchResponse response, SearchParam param){
        SearchResult result = new SearchResult();
        //1????????????????????????????????????
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
        //2?????????????????????????????????????????????
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attr_agg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //1??????????????????id;
            long attrId = bucket.getKeyAsNumber().longValue();
            //2????????????????????????
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            //3???????????????????????????
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
        //3?????????????????????????????????????????????
        List<SearchResult.BrandVo> brandVos = new ArrayList<>();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //1??????????????????id
            long brandId = bucket.getKeyAsNumber().longValue();
            //2?????????????????????
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            //3????????????????????????
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandId(brandId);
            brandVo.setBrandName(brandName);
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        //4???????????????????????????????????????
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");

        List<SearchResult.CatalogVo> catalogVos = new ArrayList<>();
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            //????????????id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));

            //???????????????
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);
            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);
        //===========??????????????????????????????=============
        //5???????????????-??????
        result.setPageNum(param.getPageNum());
        //6???????????????-????????????
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //7???????????????-?????????-??????
        int totalPages = total%EsConstant.PRODUCT_PAGESIZE == 0 ?(int) total/EsConstant.PRODUCT_PAGESIZE:((int)total/EsConstant.PRODUCT_PAGESIZE+1);
        result.setTotalPages(totalPages);
        return result;
    }
}

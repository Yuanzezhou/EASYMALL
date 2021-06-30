package com.harbin.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-20-15:23
 */

@Data
public class SkuEsModel {
/*
      "attrs":{
        "type":"nested",
        "properties": {
          "attrId":{
            "type":"long"
          },
          "attrName":{
            "type": "keyword",
            "index": false,
            "doc_values": false
          },
          "attrValue":{
            "type":"keyword"
          }
        }
 */

    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private Long brandId;

    private Long catalogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;

        private String attrName;

        private String attrValue;
    }
}

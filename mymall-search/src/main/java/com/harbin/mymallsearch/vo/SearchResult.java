package com.harbin.mymallsearch.vo;

import com.harbin.common.to.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-01-19:53
 */

@Data
public class SearchResult {


    /**
     * 查询到的商品信息
     */
    private List<SkuEsModel> products;

    private Integer pageNum;//当前页码

    private Long total;//总记录数

    private Integer totalPages;//总页码

    private List<BrandVo> brands;//当前查询到的结果，所有涉及到的品牌

    private List<CatalogVo> catalogs;//当前查询到的结果，所有涉及到的分类

    private List<AttrVo> attrs;//当前查询到的结果，所有涉及到的属性

    //=====================以上是返给页面的信息=============================

     @Data
     public static class BrandVo{

      private Long brandId;

      private String brandName;

      private String brandImg;
    }
 
     @Data
     public static class CatalogVo{
      private Long catalogId;

      private String catalogName;

      private String brandImg;
    }
 
     @Data
     public static class AttrVo{
      private Long attrId;

      private String attrName;

      private List<String> attrValue;
     }
}

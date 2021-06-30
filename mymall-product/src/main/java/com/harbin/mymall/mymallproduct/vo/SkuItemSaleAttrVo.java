package com.harbin.mymall.mymallproduct.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-03-15:58
 */
@Data
public class SkuItemSaleAttrVo {

    private Long attrId;

    private String attrName;

    private List<AttrValueWithSkuIdVo> attrValues;
}

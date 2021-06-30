package com.harbin.mymall.mymallproduct.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-03-03-15:59
 */
@Data
@ToString
public class SpuItemAttrGroupVo {

    private String groupName;

    private List<Attr> attrs;

}
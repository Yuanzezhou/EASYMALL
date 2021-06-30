package com.harbin.mymall.mymallware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-06-17:36
 */
@Data
public class MergeVo {

    private Long PurchaseId;

    private List<Long> items;

}

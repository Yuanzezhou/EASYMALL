package com.harbin.mymall.mymallware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-06-20:49
 */
@Data
public class PurchaseDoneVo {

    private Long id;

    List<PurchaseItemDoneVo> items;
}

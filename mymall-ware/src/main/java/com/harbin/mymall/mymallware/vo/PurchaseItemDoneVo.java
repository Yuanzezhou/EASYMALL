package com.harbin.mymall.mymallware.vo;

import lombok.Data;

/**
 * @author Yuanzz
 * @creat 2021-02-06-20:49
 */

@Data
public class PurchaseItemDoneVo {
    private Long itemId;

    private Integer status;

    private String Reason;
}

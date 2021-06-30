package com.harbin.common.to.mq;

import lombok.Data;

/**
 * @author Yuanzz
 * @creat 2021-03-10-14:11
 */

@Data
public class StockLockedTo {
    private Long id;//库存工作单的id；

    private StockDetailTo detailTo;

}

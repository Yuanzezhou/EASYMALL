package com.harbin.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Yuanzz
 * @creat 2021-02-05-22:49
 */

@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}

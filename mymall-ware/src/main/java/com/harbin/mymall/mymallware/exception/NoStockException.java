package com.harbin.mymall.mymallware.exception;

/**
 * @author Yuanzz
 * @creat 2021-03-08-19:56
 */
public class NoStockException extends RuntimeException {
    public NoStockException(Long skuId){
        super("商品:"+skuId+"没有足够的库存了");
    }
}

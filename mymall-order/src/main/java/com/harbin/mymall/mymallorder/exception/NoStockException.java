package com.harbin.mymall.mymallorder.exception;

/**
 * @author Yuanzz
 * @creat 2021-03-08-19:56
 */
public class NoStockException extends RuntimeException {
    public NoStockException(String msg){
        super(msg);
    }
}

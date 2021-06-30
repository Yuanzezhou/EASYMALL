package com.harbin.mymall.mymallmember.exception;

/**
 * @author Yuanzz
 * @creat 2021-03-05-13:19
 */
public class PhoneExistException extends RuntimeException {

    public PhoneExistException(){
        super("手机号存在");
    }
}

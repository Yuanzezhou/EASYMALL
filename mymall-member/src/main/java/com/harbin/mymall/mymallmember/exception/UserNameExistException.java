package com.harbin.mymall.mymallmember.exception;

/**
 * @author Yuanzz
 * @creat 2021-03-05-13:20
 */
public class UserNameExistException extends RuntimeException {

    public UserNameExistException(){
        super("用户名已存在");
    }
}

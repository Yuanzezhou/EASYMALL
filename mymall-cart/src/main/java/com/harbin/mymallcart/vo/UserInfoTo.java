package com.harbin.mymallcart.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author Yuanzz
 * @creat 2021-03-07-16:18
 */
@ToString
@Data
public class UserInfoTo {

    private Long userId;

    private String userKey; //一定封装

    private boolean tempUser = false;  //判断是否有临时用户
}

package com.harbin.mymall.mymallproduct.vo;

import lombok.Data;

/**
 * @author Yuanzz
 * @creat 2021-02-02-20:39
 */

//该类是用来封装返回给前端的数据
//Entity类是用来传给数据库保存的，与每张表一一对应。
@Data
public class AttrRespVo extends AttrVo {
    //所属分类名
    private String catelogName;

    //所属分组名
    private String groupName;

    private Long[] catelogPath;
}

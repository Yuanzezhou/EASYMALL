package com.harbin.mymall.mymallproduct.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.harbin.mymall.mymallproduct.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Yuanzz
 * @creat 2021-02-05-1:00
 */
@Data
public class AttrGroupWithAttrsVo {
    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;

    /**
     * 所有属性
     */
    private List<AttrEntity> attrs;
}

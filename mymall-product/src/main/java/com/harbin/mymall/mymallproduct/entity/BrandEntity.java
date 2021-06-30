package com.harbin.mymall.mymallproduct.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import com.harbin.common.valid.ListValue;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 品牌
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-13 20:56:07
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotEmpty(message = "品牌名不能为空哦！")
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "url不能为空！")
	@URL(message = "logo必须是一个合法的url地址！")
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue(vals={0,1})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotEmpty(message = "首字母不能为空！")
	@Pattern(regexp = "^[a-zA-Z]$")		//    /^[a-zA-Z]$  这两个斜杠不需要
	private String firstLetter;
	/**
	 * 排序
	 */
	private Integer sort;
}

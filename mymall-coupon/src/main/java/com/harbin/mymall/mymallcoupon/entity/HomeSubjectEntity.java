package com.harbin.mymall.mymallcoupon.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 首页专题表
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 23:10:44
 */
@Data
@TableName("sms_home_subject")
public class HomeSubjectEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 专题名字
	 */
	private String name;
	/**
	 * 专题标题
	 */
	private String title;
	/**
	 * 专题副标题
	 */
	private String subTitle;
	/**
	 * 显示状态
	 */
	private Integer status;
	/**
	 * 详情连接
	 */
	private String url;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 专题图片地址
	 */
	private String img;

}

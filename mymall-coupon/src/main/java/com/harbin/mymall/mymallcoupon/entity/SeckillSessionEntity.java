package com.harbin.mymall.mymallcoupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.harbin.mymall.mymallcoupon.vo.SeckillSkuVo;
import lombok.Data;

/**
 * 秒杀活动场次
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 23:10:44
 */
@Data
@TableName("sms_seckill_session")
public class SeckillSessionEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 场次名称
	 */
	private String name;
	/**
	 * 每日开始时间
	 */
	private Date startTime;
	/**
	 * 每日结束时间
	 */
	private Date endTime;
	/**
	 * 启用状态
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;

	@TableField(exist = false)	//表明不是数据库字段
	private List<SeckillSkuRelationEntity> relationEntities;
}

package com.harbin.mymall.mymallmember.dao;

import com.harbin.mymall.mymallmember.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:12:51
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}

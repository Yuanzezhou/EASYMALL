package com.harbin.mymall.mymallmember.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallmember.entity.MemberLevelEntity;

import java.util.Map;

/**
 * 会员等级
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:12:51
 */
public interface MemberLevelService extends IService<MemberLevelEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


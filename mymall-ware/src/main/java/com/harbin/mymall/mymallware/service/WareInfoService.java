package com.harbin.mymall.mymallware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.harbin.common.utils.PageUtils;
import com.harbin.mymall.mymallware.entity.WareInfoEntity;
import com.harbin.mymall.mymallware.vo.FareVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:54
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

    FareVo getFare(Long addrId);
}


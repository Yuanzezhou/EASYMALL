package com.harbin.mymall.mymallware.dao;

import com.harbin.mymall.mymallware.entity.WareOrderTaskDetailEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 库存工作单
 * 
 * @author yuanzz
 * @email 1002271900@qq.com
 * @date 2021-01-14 21:13:54
 */
@Mapper
public interface WareOrderTaskDetailDao extends BaseMapper<WareOrderTaskDetailEntity> {

    void updateLockStatus(@Param("lockStatus") Integer lockStatus,@Param("id") Long id);

    Integer getStatusByTaskId(@Param("id") Long taskId);
}

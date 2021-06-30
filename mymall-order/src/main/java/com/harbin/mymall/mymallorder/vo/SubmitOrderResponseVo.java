package com.harbin.mymall.mymallorder.vo;

import com.harbin.mymall.mymallorder.entity.OrderEntity;
import lombok.Data;

@Data
public class SubmitOrderResponseVo {
 
    private OrderEntity order;
 
    private Integer code;
 
}
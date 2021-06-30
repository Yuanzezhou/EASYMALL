package com.harbin.mymall.mymallorder.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-03-07-22:42
 */
//订单确认页需要用的数据
public class OrderConfirmVo {

    //收获地址，ums_member_receive_address表
    List<MemberAddressVo> address;

    //所有选中的购物项
    List<OrderItemVo> items;

    //发票。。。

    //优惠券信息。。。
    //积分
    Integer integration;

    //订单总额
    BigDecimal total;

    //应付价格
    BigDecimal payPrice;

    //防重令牌
    String orderToken;

    @Setter @Getter
    Map<Long,Boolean> stocks;

    //总件数
    public Integer getCount(){
        Integer i = 0;
        if (items != null){
            for (OrderItemVo item : items) {
                i += item.getCount();
            }
        }
        return i;
    }

    public List<MemberAddressVo> getAddress() {
        return address;
    }

    public void setAddress(List<MemberAddressVo> address) {
        this.address = address;
    }

    public List<OrderItemVo> getItems() {
        return items;
    }

    public void setItems(List<OrderItemVo> items) {
        this.items = items;
    }

    public Integer getIntegration() {
        return integration;
    }

    public void setIntegration(Integer integration) {
        this.integration = integration;
    }

    public BigDecimal getTotal() {
        BigDecimal total = new BigDecimal("0");
        if (items != null){
            for (OrderItemVo item : items) {
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                total = total.add(multiply);
            }
        }

        return total;
    }

    public BigDecimal getPayPrice() {
        return getTotal();
    }

    public String getOrderToken() {
        return orderToken;
    }

    public void setOrderToken(String orderToken) {
        this.orderToken = orderToken;
    }
}

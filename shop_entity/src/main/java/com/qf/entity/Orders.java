package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@Accessors(chain = true)
@TableName("t_orders")
public class Orders extends  BaseEntity{

    private String oid;
    private Integer uid;
    private BigDecimal totalPrice;
    private String person;
    private String address;//地址
    private String phone;//电话
    private String code;//邮编
    private String orderId;//订单id

    @TableField(exist = false)
    private List<OrdersDetail> ordersDetails;
}

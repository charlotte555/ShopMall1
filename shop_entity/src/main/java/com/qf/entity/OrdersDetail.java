package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@TableName("t_orders_detail")
@Accessors(chain = true)
public class OrdersDetail extends BaseEntity{

    //此张表做了字段冗余
    private Integer oid;//订单id
    private Integer gid;//商品id
    private String gtitle;//商品标题
    private String cover;
    private BigDecimal gprice;
    private Integer gnumber;
    private BigDecimal totalPrice;//小计

}

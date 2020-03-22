package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("t_shopcart")
public class ShopCart extends BaseEntity implements Serializable {

    private Integer uid;//用户id
    private Integer gid;//商品id
    private Integer gnumber;//数量
    private BigDecimal gprice;//价格
    private BigDecimal totalPrice;//小计

    //防一个商品对象,用来订单的展示
    @TableField(exist = false)
    private Goods goods;

}

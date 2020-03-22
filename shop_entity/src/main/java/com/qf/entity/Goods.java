package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_goods")
public class Goods extends BaseEntity {

    private String subject;//标题
    //bigDecimal这个数学的数据类型,在计算二进制的时候能计算的很精确,但是相比较于double来说,效率更低
    private BigDecimal price;//价格
    private Integer save;//库存
    private String info;//描述

    //表示这个字段在数据库的表里没有
    @TableField(exist = false)
    private String coverUrl;

    @TableField(exist = false)
    private List<String> otherUrls;

}

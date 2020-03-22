package com.qf.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_addresses")
public class Address {

    private String person;//收货人
    private String address;//详细地址
    private String phone;//收货电话
    private String code;//邮编
    @TableField("isdefault")
    private Integer isDefault=0;/* 是否是默认地址,0表示不是默认地址,1表示是默认地址 */
    private Integer uid;

}

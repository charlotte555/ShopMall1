package com.qf.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@TableName("t_goods_pic")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class GoodsImg extends BaseEntity {

    private String url;//图片路径
    private  String picDesc;//图片描述
    private Integer isCover;//1是封面0不是封面
    private Integer gid;//商品id
}


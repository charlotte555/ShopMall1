package com.qf.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName("t_user_ahead")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User extends BaseEntity {

    private String username;//姓名
    private String password;
    private String nickname;//昵称
    private String email;
    private String phone;
    private Integer sex;//1男0女

}

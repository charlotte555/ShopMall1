package com.qf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class Email implements Serializable {

    //设置发送邮件需要的属性
    private String from;//发送方
    private String to;//接受者
    private String subject;//邮件标题pr
    private String context;//邮件内容
    private Date sendTime;//发送邮件的时间
}

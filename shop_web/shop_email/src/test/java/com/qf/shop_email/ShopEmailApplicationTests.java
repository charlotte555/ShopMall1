package com.qf.shop_email;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;

@SpringBootTest
class ShopEmailApplicationTests {

    @Autowired
    private JavaMailSender javaMailSender;

    @Test
    void contextLoads() throws MessagingException {

        //前提条件都已经配置完成
        //创建一个邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //在设置邮件内容时不用java自带的邮件设置对象
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);

        //设置邮件的内容
        mimeMessageHelper.setSubject("找回密码");

        //发送方:和发送方的邮箱一致
        mimeMessageHelper.setFrom("erdong555@sina.com");
        //接收方:to表示只给你发
        mimeMessageHelper.setTo("1961322146@qq.com");
        mimeMessageHelper.setText("您的密码忘记,请及时找回密码");
        //设置当前时间
        mimeMessageHelper.setSentDate(new Date());
        //发送
        javaMailSender.send(mimeMessage);

        System.out.println("成功");

    }

}

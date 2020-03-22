package com.qf.listener;

import com.qf.entity.Email;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class ShopEmailListener {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    //因为交换机和队列的绑定已经在提供者那边完成,这边只用声明是哪个队列(??那怎么知道是哪个交换机:可能是在提供者那边和这个队列绑定的只有这个交换机)
    @RabbitListener(queuesToDeclare = @Queue(name = "email_queue"))
    public void msgHandler(Email email) throws MessagingException {

        System.out.println("hhh"+email);
        //rabbitMQ前提条件都已经配置完成,在提供者那边已经将内容提交到rabbitMQ中,消费者可以传对象拿
        //创建一个邮件对象
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        //在设置邮件内容时不用java自带的邮件设置对象
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage);

        //设置邮件的标题
        mimeMessageHelper.setSubject(email.getSubject());

        //发送方:和发送方的邮箱一致
        System.out.println(from);
        mimeMessageHelper.setFrom(from);
        //接收方:to表示只给你发
        mimeMessageHelper.setTo(email.getTo());
        //设置邮件的内容
        mimeMessageHelper.setText(email.getContext());

        //设置当前时间
        mimeMessageHelper.setSentDate(email.getSendTime());
        //发送
        javaMailSender.send(mimeMessage);

        System.out.println("邮件"+email);

        System.out.println("成功");

    }
}

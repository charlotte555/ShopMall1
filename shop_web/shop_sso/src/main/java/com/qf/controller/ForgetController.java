package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Email;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.ISsoService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequestMapping("forget")
@Controller
public class ForgetController {


    @Reference
    private ISsoService ssoService;

    //使用rabbitMQ向邮件工程连接
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 跳转到忘记密码页面:这个页面需要填写账号,通过账号来找密码
     * @return
     */
    @RequestMapping("toForgetPassword")
    public String forgetEmail() {
        return "forgetPass";
    }

    @RequestMapping("sendEmail")
    @ResponseBody
    public ResultData<Map<String,String>> sendEmail(String username) {

        //
        User user=ssoService.getByUsername(username);

        if(user==null){
            return  new ResultData<Map<String, String>>().setCode(ResultData.ResultDataList.aheadCode).setMsg("该用户不存在");
        }

        //携带一个uuid发过去给发送邮件
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(uuid,username);
        //设置这个uuid的超时时间
        redisTemplate.expire(uuid,5, TimeUnit.MINUTES);

        String url="http://localhost:8082/forget/toUpdatePass?token="+uuid;
        Email email=new Email()
                //从数据库里可以查到该用户原来的邮箱
                .setTo(user.getEmail())
                .setSubject("找回密码")
                .setContext("点击<a href='"+url+"'>这里</a>找回密码")
                .setSendTime(new Date());

        //向rabbitMQ发送数据
        rabbitTemplate.convertAndSend("email_exchange","",email);

        /**
         * 向前台做一个收件邮箱的提示,
         * 情景:在点击找回密码的按钮之后,在旁边提示一个你的邮件已经发送到了xxx邮箱的信息
         */
        //a.先获取收件邮箱
        String email1 = user.getEmail();
        //b.截取收件邮箱的部分信息,将这些信息替换成xxxx
        String substring = email1.substring(2,email1.lastIndexOf("@"));

        String emailMi = email1.replace(substring, "********");

        //c.还要传一个接收邮箱的官方登录地址:这个地址一般有固定格式
        String emailAddress="mail."+email1.substring(email1.lastIndexOf("@")+1);//这个得到的是一个类似于qq.com的格式

        //新建一个map集合来装这个看不见的给用户看的收件邮箱,和邮箱官方地址
        Map<String,String> map=new HashMap<String, String>();
        map.put("emailMi",emailMi);
        map.put("emailAddress",emailAddress);

        //根据用户名找回密码,发送邮件
        return new ResultData<Map<String,String>>().setCode(ResultData.ResultDataList.okCode).setMsg("发送成功").setData(map);
    }

    //修改密码
    @RequestMapping("toUpdatePass")
    public String updatePass(String token){
      return "toUpdatePass";
    }

    @RequestMapping("/updatePass")
    public String updatePass(String token,String newPassword){

        String username = redisTemplate.opsForValue().get(token);
        if (username==null){
            return "errorPass";
        }
        ssoService.updatePass(username,newPassword);
        //在修改完密码之后删除uuid
        redisTemplate.delete(token);
        return "login";
    }

}

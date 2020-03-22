package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.ISsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequestMapping("/sso")
@Controller
public class SsoController {


    @Reference
    private ISsoService ssoService;

    //这个redis对象不用序例化???
    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/toregist")
    public String toRegister(){
        return "register";
    }
    @RequestMapping("/tologin")
    public String toLogin(){
        return "login";
    }

    /**
     * zhuce
     */
    @RequestMapping("/register")
    public String register(User user, Model model){
        Integer i=ssoService.register(user);

        if (i>0) {
            return "login";
        }else if (i==-1){
            model.addAttribute("errorMsg", "该用户已经注册了");
        }else {
            model.addAttribute("errorMsg","注册失败");
        }
        return "register";
    }

    //登录
    @RequestMapping("login")
    public String login(String username, String password, Model model, HttpServletResponse response,String returnUrl){

        User user = ssoService.getByUsername(username);

        /**
         * 保存登录状态(使用redis):
         * 1.将登录信息存放在redis里面,key(uuid)-value(用户对象)
         * 2.将用户的登录信息写进浏览器的cookie里面,浏览器端保存用户登录的uuid,
         * 过程:使用redis,浏览器有用户要访问其他工程时,向服务器发送请求,会将cookie里面的uuid带到服务器,
         * 再根据uuid从redis里面取出用户信息
         */

        String token = UUID.randomUUID().toString();
        //将用户信息放进redis里面
        redisTemplate.opsForValue().set(token,user);
        //设置数据存放在redis的超时时间???
        redisTemplate.expire(token,7, TimeUnit.DAYS);

        //将token放到浏览器cookie里
        Cookie cookie=new Cookie("loginToken",token);
        cookie.setMaxAge(60*60*24*7);
        //设置好域名,并且这个字段不能设置成顶级域名,牵扯到一级域名的单点登录
        cookie.setDomain("localhost");
        //设计访问的路径
        cookie.setPath("/");
        //只有服务能修改该cookie,页面上的脚本不能修改该cookie
        cookie.setHttpOnly(false);
        //只有https的协议下,服务器才会收到cookie
//        cookie.setSecure(true);

        response.addCookie(cookie);

        //如果传来的路径为空则跳转到首页
        if (returnUrl==null&&returnUrl.equals("")){

            return "redirect:http://localhost:8888";
        }

        try {
            returnUrl = URLEncoder.encode(returnUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //如果用户名不为空并且密码是对的
        if (user!=null&&user.getPassword().equals(password)){

            return "redirect:http://localhost:8083/cart/merge?returnUrl="+returnUrl;

        }else {

            model.addAttribute("errorMsg","你输入的账号或者密码错误");
            return "login";
        }
    }

//    @RequestMapping("isLogin")
//    @ResponseBody
//    public String isLogin(@CookieValue(name="loginToken",required = false) String loginToken, String callback){
//        /**
//         * 使用jsonp需要在controller的请求这边传一个方法过去,jsonp帮你封装好的方法名叫做callback
//         */
//        System.out.println("啦啦啦啦"+loginToken+callback);
//        //如果没有登录先向前台响应
//        ResultData<Object> resultData = new ResultData<Object>().setCode(ResultData.ResultDataList.backCode).setMsg("no");
//
//        //拿到cookie里面token的值,并根据token的值获取到已经登录的用户对象
//        if (loginToken!=null){
//            User user = (User) redisTemplate.opsForValue().get(loginToken);
//            if (user!=null){
//                //用户登录了向响应前台200
//                resultData.setCode(ResultData.ResultDataList.okCode).setMsg("yes").setData(user);
//            }
//        }
//        return callback!=null?callback+ "("+ JSON.toJSONString(resultData)+ ")":JSON.toJSONString(resultData);
//    }


    /**
     * 使用springmvc的注解和
     * @param loginToken
     * @return
     */
    @RequestMapping("isLogin")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public String isLogin(@CookieValue(name="loginToken",required = false) String loginToken){

        ResultData<Object> resultData=new ResultData<Object>();

        if (loginToken!=null){
            User user = (User) redisTemplate.opsForValue().get(loginToken);
            if (user!=null){
               return JSON.toJSONString(resultData.setCode(ResultData.ResultDataList.okCode).setData(user));
            }
        }
        return JSON.toJSONString(resultData.setCode(ResultData.ResultDataList.aheadCode));
    }
    /**
     * 注销
     * @return
     */
    @RequestMapping("/loginout")
    public String loginout(@CookieValue(name="loginToken",required = false) String loginToken,HttpServletResponse response){

        redisTemplate.delete(loginToken);

        Cookie cookie=new Cookie("loginToken",null);
        cookie.setMaxAge(0);
        //路径和域名要和原来登录时设置的 域名保持一致
        cookie.setPath("/");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        return "redirect:/sso/tologin";

    }


}

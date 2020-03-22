package com.qf.aop;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Component
@Aspect
public class loginAop {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 增强方法
     * 增强哪些指定的方法
     * @return
     */
    //表示增强加了下面注解的方法
    @Around("@annotation(isLogin)")
    //环绕增强不能放多余的参数
    public Object isLogin(ProceedingJoinPoint point) throws UnsupportedEncodingException {

        /**
         * 判断当前登录状态
         * 1.获取到cookie里面的token值
         * 2根据token拿到user对象
         * 3.跟redis的user对象进行对比,判断当前用户是否登
         * 4.如果为空:判断mustLogin是否为true-->强制跳转到登录页面
         * 如果不为空,保存用户信息,controller层能获得开发者信息
         *
         */

        //1.拿cookie
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String loginToken = null;
        HttpServletRequest request = requestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();

        //对cookies判空,以防出现什么莫名其妙的错误
        if (null!=cookies) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("loginToken")) {
                    loginToken = cookie.getValue();
                    break;
                }
            }
        }
        System.out.println("login"+loginToken);

        User user = null;
        //判断是否登录
        if (loginToken!=null){
            //不好去redis去拿,直接调用isLogin的方法(这个方法是用来判断当前用户是否登录,并在页面头部给出信息提示的controller)
            //以上操作不好实现
             user = (User) redisTemplate.opsForValue().get(loginToken);
        }
        if (user==null){
            //未登录,判断@isLogin的注解返回值

            System.out.println("user的值是null");
            //首先获得@isLogin注解
            MethodSignature methodSignature= (MethodSignature) point.getSignature();
            Method method = methodSignature.getMethod();
            isLogin isLogin = method.getAnnotation(isLogin.class);

            boolean b = isLogin.mustLogin();
            if (b){
                String returnUrl = request.getRequestURL().toString();

                 returnUrl = URLEncoder.encode(returnUrl, "utf-8");

                 String loginUrl="http://localhost:8082/sso/tologin?returnUrl="+returnUrl;
                 return "redirect:"+loginUrl;
            }
        }

        //
        UserHolder.setUser(user);

        //增强的操作
//        System.out.println("前置");
        //调用目标方法
        Object proceed = null;
        //将这个做成统一异常处理,这样都会抛会controller层,这样会拦截住这个
        try {
             proceed = point.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

//        System.out.println("后置");
        UserHolder.setUser(null);
        return proceed;
    }

}

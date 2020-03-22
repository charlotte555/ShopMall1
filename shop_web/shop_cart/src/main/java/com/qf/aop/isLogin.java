package com.qf.aop;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface isLogin {

    //设置注解方法
    //后面的default表示默认值,如果这边不给默认值,那么在调用这个注解的时候必须给默认值
    boolean mustLogin() default false;
}

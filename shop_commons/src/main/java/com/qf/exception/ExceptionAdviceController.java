package com.qf.exception;
import com.qf.entity.ResultData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 这个类是为了解决代码异常
 * 和与页面交互的同步和异步的异常
 * 全局跳转的页面
 */
@ControllerAdvice
public class ExceptionAdviceController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object HandlerException(HttpServletRequest request) {

        //同步与异步发送的请求的请求头不同
        String header = request.getHeader("X-Requested-With");

        if (null != header && header.equals("XMLHttpRequest")) {
            //当前是ajax请求
            System.out.println("ajax请求异常");
            return new ResultData().setCode(ResultData.ResultDataList.backCode).setMsg("服务器繁忙,请稍后再试");
        } else {

            System.out.println("返回前端页面异常");
            return new ModelAndView("500");
        }
    }
}

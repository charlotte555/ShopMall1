package com.qf.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

//解决用户输入地址有误等的问题
@Controller
public class SystemExceptionController implements ErrorController {

    @RequestMapping("/error")
    public String systemException(HttpServletResponse response){

        int status = response.getStatus();

        //根据前台传来的错误码跳到不同的页面
        switch (status){
            case 401:
                return "401";
            case 402:
                return "402";
            case 403:
                return "403";
            case 404:
                return "404";
            case 405:
                return "405";
        }
        return "myError";

    }

    @Override
    public String getErrorPath() {

        System.out.println("输入的地址异常");
        return "/error";
    }
}

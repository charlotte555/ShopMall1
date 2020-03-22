package com.qf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/toPage")
public class PageController {

    @RequestMapping("/{page}")
    public String page(@PathVariable("page") String page){
        //以restful风格来将传路径
        return page;
    }
}

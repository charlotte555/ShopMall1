package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("search")
public class SearchController {

    @Reference
    private ISearchService searchService;
    //全局搜索
    @RequestMapping("searchByKeyWord")
    public String searchAll(String keyWord, Model model){

        System.out.println("前台传来的关键字"+keyWord);

        List<Goods> list=searchService.getSolrbyKeyWord(keyWord);

        model.addAttribute("list",list);
        return "searchList";
    }
}

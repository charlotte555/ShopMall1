package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/item")
public class ItemController {

    @Reference
    private IGoodsService goodsService;

    @RequestMapping("/showItemById")
    public String showItemById(Integer id, Model model){

        System.out.println(id);
        Goods goods=goodsService.getById(id);
        System.out.println("查询到的这个商品"+goods);
        model.addAttribute("goods",goods);

        return "goodsItem";
    }
}

package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.UserHolder;
import com.qf.aop.isLogin;
import com.qf.entity.Address;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/order")
@Controller
public class OrderController {

    @Reference
    private IOrderService orderService;
    @Reference
    private IAddressService addressService;
    @Reference
    private ICartService cartService;

    @RequestMapping("/toOrder")
    @isLogin(mustLogin = true)
    public String addOrder(Integer[] gid, Model model){

        //从购物车跳转到订单页面
        User user = UserHolder.getUser();
        //1.查询订单,从购物车里查询

        List<ShopCart> shopCarts=cartService.queryCartByGids(gid,user.getId());
        //2.查询收货地址
        List<Address> list=addressService.queryAddressByUid(user.getId());

        model.addAttribute("address",list);
        model.addAttribute("shopCarts",shopCarts);

        return "ordersList";
    }
}

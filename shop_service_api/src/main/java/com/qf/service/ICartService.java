package com.qf.service;

import com.qf.entity.ShopCart;
import com.qf.entity.User;

import java.util.List;

public interface ICartService {
    String addShopCart(User user, ShopCart shopCart, String cartToken);

    List<ShopCart> getShopCartListByUid(Integer id);

    Integer deleteCart(Integer id);

    List<ShopCart> queryCartByGids(Integer[] gid, Integer id);
}


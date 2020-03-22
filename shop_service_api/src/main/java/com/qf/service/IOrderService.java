package com.qf.service;

import com.qf.entity.Orders;
import com.qf.entity.ShopCart;

public interface IOrderService {
    Orders addOrder(ShopCart shopCart);
}

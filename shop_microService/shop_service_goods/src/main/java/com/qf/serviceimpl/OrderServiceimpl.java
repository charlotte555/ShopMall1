package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IOrderMapper;
import com.qf.entity.Orders;
import com.qf.entity.ShopCart;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Service
public class OrderServiceimpl implements IOrderService {

    @Autowired
    private IOrderMapper orderMapper;

    @Override
    public Orders addOrder(ShopCart shopCart) {
        System.out.println("hhhh"+shopCart.getTotalPrice());

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Orders orders=new Orders()
                .setOrderId(uuid).setTotalPrice(shopCart.getTotalPrice())
                .setUid(shopCart.getUid());
        int i = orderMapper.insert(orders);
        return null;
    }
}

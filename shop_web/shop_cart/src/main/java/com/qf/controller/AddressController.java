package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.aop.UserHolder;
import com.qf.aop.isLogin;
import com.qf.entity.Address;
import com.qf.entity.ResultData;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/address")
@Controller
public class AddressController {

    @Reference
    private IAddressService addressService;

    @RequestMapping("/addAddress")
    @isLogin(mustLogin = true)
    public ResultData<String> addAdress(Address address){
        User user = UserHolder.getUser();
        address.setUid(user.getId());
        Integer i=addressService.addAddress(address);
        return new ResultData<String>();
    }
}

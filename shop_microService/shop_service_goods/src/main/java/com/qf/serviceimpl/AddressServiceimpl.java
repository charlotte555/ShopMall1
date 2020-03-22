package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IAddressMapper;
import com.qf.entity.Address;
import com.qf.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class AddressServiceimpl implements IAddressService {

    @Autowired
    private IAddressMapper addressMapper;
    @Override
    public List<Address> queryAddressByUid(Integer id) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uid",id);
        return addressMapper.selectList(queryWrapper);
    }

    @Override
    public Integer addAddress(Address address) {
        return addressMapper.insert(address);
    }
}

package com.qf.service;

import com.qf.entity.Address;

import java.util.List;

public interface IAddressService {

    List<Address> queryAddressByUid(Integer id);

    Integer addAddress(Address address);
}

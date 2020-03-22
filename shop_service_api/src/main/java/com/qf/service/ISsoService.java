package com.qf.service;

import com.qf.entity.User;

public interface ISsoService {

    //注册
    Integer register(User user);

    User getByUsername(String username);

    void updatePass(String username, String newPassword);
}

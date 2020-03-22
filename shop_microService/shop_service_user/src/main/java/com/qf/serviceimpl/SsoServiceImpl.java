package com.qf.serviceimpl;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.ISsoMapper;
import com.qf.entity.User;
import com.qf.service.ISsoService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class SsoServiceImpl implements ISsoService {

    @Autowired
    private ISsoMapper ssoMapper;
    /**
     * 注册
     * @param user
     * @return
     */
    @Override
    public Integer register(User user) {
        //先查询数据库里面有没有你要注册的这个姓名
        QueryWrapper queryWrapper=new QueryWrapper();

        queryWrapper.eq("username",user.getUsername());

        Integer count = ssoMapper.selectCount(queryWrapper);
        System.out.println("从数据库里查询的结果"+count);
        //说明这个人已经存在了
        if (count==1){
            return -1;
        }
        return ssoMapper.insert(user);
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        return ssoMapper.selectOne(queryWrapper);
    }

    @Override
    public void updatePass(String username,String newPassword) {
        //先查询这个站账号有木有
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("username",username);
        User user = ssoMapper.selectOne(queryWrapper);

        User user1 = user.setPassword(newPassword);
         ssoMapper.updateById(user1);

    }
}

package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.ICartMapper;
import com.qf.entity.Goods;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartMapper cartMapper;

    @Autowired
    private RedisTemplate redisTemplate;//选取需要序列化的模板,向redis里面添加数据
    @Autowired
    private IGoodsService goodsService;

    @Override
    @Transactional
    public String addShopCart(User user, ShopCart shopCart, String cartToken) {

        //根据商品id先去商品表里面查询该添加购物车商品的信息
        Goods goods = goodsService.getById(shopCart.getGid());
        //小计
        BigDecimal totalPrice=goods.getPrice().multiply(BigDecimal.valueOf(shopCart.getGnumber()));
        shopCart.setTotalPrice(totalPrice).setGprice(goods.getPrice());
        //根据用户登录状态向不同的地方添加数据库
        if (user!=null){
            //根据用户id,向数据库里添加数据
            shopCart.setUid(user.getId());
            //已经登录向数据库里添加数据
            cartMapper.insert(shopCart);
        }else {
            //用户未登录,向redis里添加
            String uuid = UUID.randomUUID().toString();
            //采用redis的属性list用来放购物车,因为购物车是最先添加的在最上面,所以可以选用从左添加数据的方式
            //如果传过来的cartToken是null的话,创建一个uuid
            cartToken=cartToken!=null?cartToken:uuid;

            redisTemplate.opsForList().leftPush(cartToken,shopCart);
            System.out.println(cartToken);
        }
        return cartToken;
    }

    /**
     * 查询购物车里的商品数据
     * @param uid
     * @return
     */
    @Transactional
    @Override
    public List<ShopCart> getShopCartListByUid(Integer uid) {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uid", uid);
        List<ShopCart> list= cartMapper.selectList(queryWrapper);
        Goods goods = null;
        for (ShopCart shopCart : list) {
            Integer gid1 = shopCart.getGid();
             goods = goodsService.getById(gid1);
            shopCart.setGoods(goods);
        }
        return list;
    }

    @Override
    @Transactional
    public Integer deleteCart(Integer id) {
        System.out.println("ssada"+id);
        return cartMapper.deleteById(id);
    }

    @Override
    public List<ShopCart> queryCartByGids(Integer[] gid, Integer uid) {

        QueryWrapper queryWrapper=new QueryWrapper();
        for (Integer id : gid) {
            queryWrapper.in("gid",id);
        }
        queryWrapper.eq("uid",uid);
        return cartMapper.selectList(queryWrapper);
    }
}

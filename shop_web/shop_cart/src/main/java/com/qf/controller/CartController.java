package com.qf.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qf.aop.UserHolder;
import com.qf.aop.isLogin;
import com.qf.entity.Goods;
import com.qf.entity.ResultData;
import com.qf.entity.ShopCart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequestMapping("/cart")
@Controller
public class CartController {

    @Reference
    private ICartService cartService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IGoodsService goodsService;

    /**
     * 在商品详情页面使用ajax跳转到这个方法,会有跨域的问题,jsonp解决
     * @param cartToken
     * @param gid
     * @param gnumber
     * @param response
     * @return
     */
    //添加购物车
    @RequestMapping("/addCart")
    @isLogin
    @ResponseBody
    public String addCart(@CookieValue(name="cartToken",required = false)String cartToken, Integer gid, Integer gnumber,String callback, HttpServletResponse response){
        //拿到登录的用户信息
        User user = UserHolder.getUser();
        /**
         * 添加购物车
         * 1.未登录--->redis(需要一个cartToken来判断)
         * 2.已登录--->放数据库
         */
        //把添加操作放在业务层
        System.out.println("user里面有值吗 "+user);

        ShopCart shopCart=new ShopCart().setGid(gid).setGnumber(gnumber);
        cartToken=cartService.addShopCart(user,shopCart,cartToken);
        System.out.println("cart"+cartToken);

        //将购物车的token写到cookie里面
        Cookie cookie=new Cookie("cartToken",cartToken);
        cookie.setMaxAge(60*60*365*24);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        //将cookie传到前台里
        response.addCookie(cookie);

        ResultData<String> resultData=new ResultData<String>().setCode(ResultData.ResultDataList.okCode).setMsg("添加成功");

        return callback+"("+JSON.toJSONString(resultData)+")";
    }

    /**
     * 已经登录添加到数据库的
     * 展示已经添加到购物车里的商品
     * @param model
     * @return
     */

    @RequestMapping("/showShopCart")
    @isLogin(mustLogin = true)
   public String showShopCart(Model model){
        //根据用户id查询,该用户的添加的订单信息
        User user = UserHolder.getUser();
        List<ShopCart> list = null;
        if (null!=user) {
             list = cartService.getShopCartListByUid(user.getId());
//        System.out.println("购物车里的数据"+list);
        }
        System.out.println("asda"+list);
        model.addAttribute("shopCart", list);
        return "shopCartList";
   }

    /**
     * 此方法是没有登录添加到购物车里
     * ajax默认不携带cookie
     * 涉及到跨域问题:使用注解和前端头设置
     *没有登录添加购物车,从redis里面查询
     * @return
     */
    @CrossOrigin(allowCredentials = "true")
    @RequestMapping("/selectCartListByToken")
    @ResponseBody
   public String selectCartListByToken(@CookieValue(name = "cartToken",required = false) String cartToken){

        List<ShopCart> shopCarts = null;
        if (null!=cartToken){
        Long size = redisTemplate.opsForList().size(cartToken);

         shopCarts = redisTemplate.opsForList().range(cartToken,0, size);

            for (ShopCart shopCart : shopCarts) {
                Integer gid = shopCart.getGid();
                Goods goods = goodsService.getById(gid);
                shopCart.setGoods(goods);
            }
        }
        return JSON.toJSONString(shopCarts);
   }

    /**
     *合并工程
     * @param returnUrl
     * @param cartToken
     * @return
     */
    @RequestMapping("/merge")
    @isLogin(mustLogin = true)
   public String merge(String returnUrl,@CookieValue(value = "cartToken",required = false) String cartToken,HttpServletResponse response){

        if (null!=cartToken) {
            //先查询redis里面的集合长度
            Long size = redisTemplate.opsForList().size(cartToken);
            //遍历redis的购物车集合
            List<ShopCart> list = redisTemplate.opsForList().range(cartToken, 0, size);

            //将临时购物车保存到数据库中
            User user = UserHolder.getUser();
            for (ShopCart shopCart : list) {
                cartService.addShopCart(user, shopCart, cartToken);
            }
            //将临时购物车添加到数据库中,删除到redis的临时购物车
            redisTemplate.delete(cartToken);
        }

        //删除购物车的cookie
        Cookie cookie=new Cookie("cartToken",null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:"+returnUrl;
    }

    /**
     * 删除购物车
     * @return
     */
    @RequestMapping("/deleteCart/{id}")
    public String deleteCart(@PathVariable("id") Integer id) {
        System.out.println("id为:"+id);
        cartService.deleteCart(id);
        System.out.println("hhhh");
        return "redirect:/cart/showShopCart";
    }
}

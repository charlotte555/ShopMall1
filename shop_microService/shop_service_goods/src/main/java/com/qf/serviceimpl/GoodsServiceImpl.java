package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IGoodsImgMapper;
import com.qf.dao.IGoodsMapper;
import com.qf.entity.Goods;
import com.qf.entity.GoodsImg;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service//dubbo里的service的注解
public class GoodsServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsMapper goodsMapper;

    //以商品服务为消费者调用搜索服务
    @Reference
    private ISearchService searchService;

    @Autowired
    private IGoodsImgMapper goodsImgMapper;

    //注入rabbitMQ
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Goods> getGoodsList() {
        return goodsMapper.selectList(null);
    }

    @Override
    @Transactional//开启事物
    public Integer addGoods(Goods goods) {
        //向两个不同的表里面添加数据

        //向商品表里添加数据
        goodsMapper.insert(goods);

        //向图片表里添加图片
        //假设只添加封面(如果一起添加封面和商品的多个图片)
        GoodsImg goodsImg=new GoodsImg().setGid(goods.getId()).setIsCover(1).setUrl(goods.getCoverUrl());
        goodsImgMapper.insert(goodsImg);

        //多个图片添加
        for (String otherUrl :goods.getOtherUrls()){
            GoodsImg goodsImgs=new GoodsImg().setGid(goods.getId()).setIsCover(0).setUrl(otherUrl);
            goodsImgMapper.insert(goodsImgs);
        }

        //向索引库添加商品信息
//        searchService.insertSolr(goods);
        //商品服务调用搜索服务向索引库添加数据,使用rabbitMQ来实现
        rabbitTemplate.convertAndSend("goods_exchange","",goods);
        return 1;

    }

    //多表查询
    @Transactional(readOnly = true)
    @Override
    public List<Goods> getGoodsLists() {
        return goodsMapper.getGoodsLists();
    }

    @Override
    public Goods getById(Integer id) {
        return goodsMapper.getById(id);
    }

}

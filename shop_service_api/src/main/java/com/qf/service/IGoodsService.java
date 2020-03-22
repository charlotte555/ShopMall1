package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsService {
    List<Goods> getGoodsList();

    Integer addGoods(Goods goods);

    List<Goods> getGoodsLists();

    Goods getById(Integer id);
}

package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Goods;

import java.util.List;

public interface IGoodsMapper extends BaseMapper<Goods> {

    List<Goods> getGoodsLists();

    Goods getById(Integer id);
}

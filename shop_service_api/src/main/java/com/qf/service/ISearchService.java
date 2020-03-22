package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchService {

    //添加索引库
    int insertSolr(Goods goods);

    //根据关键字查询索引库
    List<Goods> getSolrbyKeyWord(String keyWord);
}

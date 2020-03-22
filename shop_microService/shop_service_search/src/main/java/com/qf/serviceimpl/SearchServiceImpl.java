package com.qf.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements ISearchService {

    //注入实现类
    @Autowired
    private SolrClient solrClient;

    //添加商品
    @Override
    public int insertSolr(Goods goods) {

        //1.创建文档对象
        //一个document相当于数据库一行记录-->一个对象
        SolrInputDocument document=new SolrInputDocument();
        //和solr容器里面配置的属性统一
        document.addField("id",goods.getId() + "");//需要一个主键id
        document.addField("subject",goods.getSubject());
        document.addField("info",goods.getInfo());
        //在solr容器里面放的price是double类型,所以需要但是实体类存放的是bigdecimal类型,它会自动转换为String类型,所以需要转成double类型
        document.addField("price",goods.getPrice().doubleValue());
        document.addField("cover",goods.getCoverUrl());
        document.addField("save",goods.getSave());

        try {
            solrClient.add(document);
            //进行数据的修改必须要commit
            solrClient.commit();

            return 1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Goods> getSolrbyKeyWord(String keyWord) {

        SolrQuery solrQuery=new SolrQuery();
        //对传进来的keyword进行判空
        if (keyWord!=null&&!keyWord.equals("")){
        solrQuery.setQuery("subject:"+keyWord+" || info:"+keyWord);
        }else {
            solrQuery.setQuery("*:*");
        }

        //设置搜索的高亮
        solrQuery.setHighlight(true);//开启高亮
        //在前缀和后缀设置高亮信息
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");
        solrQuery.addHighlightField("subject");

        try {
            QueryResponse query = solrClient.query(solrQuery);
            SolrDocumentList list = query.getResults();

            //需要对从索引库里搜出来的对象进行高亮的设置
            /**
             * map的意义:
             * Map<id,商品信息>
             * 商品信息:map<高亮的属性,需要高亮的集合>
             */
            Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
            //建一个数组来装
            List<Goods> list1=new ArrayList<Goods>();

            for (SolrDocument solrDocument:list){
                    Goods goods=new Goods();
                    goods.setId(Integer.parseInt((String) solrDocument.get("id")));
                    goods.setSubject(solrDocument.get("subject")+"")
                            .setSave((Integer) solrDocument.get("save"))
                            .setPrice(BigDecimal.valueOf((Double) solrDocument.get("price")))
                            .setCoverUrl(solrDocument.get("cover")+"");

                    //对设置高亮
                if (highlighting.containsKey(goods.getId()+"")){
                    //依次获取高亮的字段
                    //根据商品id获取需要高亮的商品集合
                    Map<String, List<String>> stringListMap = highlighting.get(goods.getId() + "");
                    //获取需要设置高亮的字段
                    List<String> subject = stringListMap.get("subject");
                    //对高亮字段进行判断
                    if (subject!=null){
                        goods.setSubject(subject.get(0));
                    }
                }
                list1.add(goods);
            }
            return  list1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

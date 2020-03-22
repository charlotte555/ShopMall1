package com.qf.listener;

import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

   @Autowired
   private ISearchService searchService;



   //消费者声明 队列和交换机绑定
    //处理提供者向交换机传的消息
    //自定义注解:(自认为:绑定队列和交换机,并且在这边创建一个队列)
    @RabbitListener(
    bindings = @QueueBinding
    (exchange = @Exchange(value = "goods_exchange",type = "fanout"),
    value = @Queue(name = "search_queue")
    ))
   public void msgHandler(Goods goods){
        System.out.println("s搜索服务接收到的商品服务数据"+goods);
       searchService.insertSolr(goods);

   }

}

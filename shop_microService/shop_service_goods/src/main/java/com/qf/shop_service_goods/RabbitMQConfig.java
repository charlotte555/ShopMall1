package com.qf.shop_service_goods;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //提供者声明交换机
    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange("goods_exchange");
    }
}

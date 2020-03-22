package com.qf.shop_sso;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //提供者创建一个广播交换机
    @Bean
    public FanoutExchange getExchange(){

        return  new FanoutExchange("email_exchange");
    }

    //在提供者这边创建队列
    @Bean
    public Queue getQueue(){
        return  new Queue("email_queue");
    }

    @Bean
    public Binding getBinging(Queue getQueue, FanoutExchange getExchange){
        //交换机绑定队列,
        return BindingBuilder.bind(getQueue).to(getExchange);
    }

}

package com.qf.shop_service_goods;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.qf")
//扫描serviceimpl的类
@DubboComponentScan("com.qf.serviceimpl")
//扫描dao层
@MapperScan("com.qf.dao")
//开启事物
@EnableTransactionManagement
public class ShopServiceGoodsApplication {

    public static void main(String[] args) {

        SpringApplication.run(ShopServiceGoodsApplication.class, args);
    }

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
//         paginationInterceptor.setOverflow(false);//5 6
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
//         paginationInterceptor.setLimit(500);
        return paginationInterceptor;
    }

}

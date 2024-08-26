package com.xi.cloud;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/6 16:08:34
 * @description
 */
@SpringBootApplication
@MapperScan("com.xi.cloud.mapper")
@EnableDiscoveryClient// 开启服务发现 spring cloud consul 服务发现
@RefreshScope// 自动刷新 consul中的配置变更时自动刷新
public class Main8001 {
    public static void main(String[] args) {
        SpringApplication.run(Main8001.class,args);
    }
}
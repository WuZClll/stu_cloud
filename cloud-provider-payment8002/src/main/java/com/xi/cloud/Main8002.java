package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author Wu
 * @date 2024/8/11 19:55:21
 * @description
 */
@SpringBootApplication
@MapperScan("com.xi.cloud.mapper")
@EnableDiscoveryClient// 开启服务发现 spring cloud consul 服务发现
@RefreshScope// 自动刷新 consul中的配置变更时自动刷新
public class Main8002 {
    public static void main(String[] args) {
        SpringApplication.run(Main8002.class,args);
    }
}
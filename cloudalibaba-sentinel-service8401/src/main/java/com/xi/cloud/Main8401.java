package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/9 20:48:02
 * @description 整合sentinel 实现熔断与限流
 */
@EnableDiscoveryClient// 服务发现
@SpringBootApplication
public class Main8401 {
    public static void main(String[] args) {
        SpringApplication.run(Main8401.class,args);
    }
}


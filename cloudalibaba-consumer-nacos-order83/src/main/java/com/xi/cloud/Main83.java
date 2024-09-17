package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/5 19:56:19
 * @description
 */
@EnableDiscoveryClient // 启用服务注册发现
@SpringBootApplication
// 优化：OpenFeign和Sentinel集成实现fallback服务降级
@EnableFeignClients // 启用feign
public class Main83 {
    public static void main(String[] args) {
        SpringApplication.run(Main83.class,args);
    }
}
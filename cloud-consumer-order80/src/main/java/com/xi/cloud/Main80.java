package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Wu
 * @date 2024/8/9 15:27:34
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient// 开启服务发现 spring cloud consul 服务发现
public class Main80 {
    public static void main(String[] args) {
        SpringApplication.run(Main80.class, args);
    }
}
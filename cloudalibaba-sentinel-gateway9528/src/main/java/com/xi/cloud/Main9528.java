package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/22 16:06
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient// 服务发现
public class Main9528 {
    public static void main(String[] args) {
        SpringApplication.run(Main9528.class, args);
    }
}
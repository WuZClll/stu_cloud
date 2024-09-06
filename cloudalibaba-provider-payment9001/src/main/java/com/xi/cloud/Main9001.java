package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/5 19:19:30
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient// 开启服务注册发现
public class Main9001 {
    public static void main(String[] args) {
        SpringApplication.run(Main9001.class,args);
    }
}
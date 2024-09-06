package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/6 19:39:10
 * @description
 */
@EnableDiscoveryClient // 开启服务注册发现
@SpringBootApplication
public class Main3377 {
    public static void main(String[] args) {
        SpringApplication.run(Main3377.class,args);
    }
}
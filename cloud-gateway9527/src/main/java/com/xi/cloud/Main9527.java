package com.xi.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/22 10:19:53
 * @description
 */
@SpringBootApplication
@EnableDiscoveryClient //服务注册和发现   spring cloud consul 服务发现
public class Main9527 {
    public static void main(String[] args) {
        SpringApplication.run(Main9527.class,args);
    }
}
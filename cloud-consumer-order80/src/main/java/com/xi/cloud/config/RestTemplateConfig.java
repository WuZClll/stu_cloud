package com.xi.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Wu
 * @date 2024/8/9 16:05:26
 * @description RestTemplate配置类
 *  RestTemplate提供了多种便捷访问远程Http服务的方法
 *  ，是一种简单便捷的访问restful服务模板类，是Spring提供的用于访问Rest服务的客户端模板工具集
 */
@Configuration// 使用 @Configuration 标记类作为配置类替换 xml 配置文件
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package com.xi.cloud.handler;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/17 15:16:41
 * @description 自定义请求来源处理器转换 定义sentinel授权规则应用属性名
 */
@Component
public class MyRequestOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        // 通过serverName来设定是白名单还是黑名单 定义sentinel授权规则的授权应用属性为serverName
        // 测试使用：http://localhost:8401/empower?serverName=?
        return httpServletRequest.getParameter("serverName");
    }
}

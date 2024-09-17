package com.xi.cloud.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/17 15:12:09
 * @description 授权
 * 在Sentinel的授权规则中，提供了 白名单与黑名单 两种授权类型。白放行、黑禁止
 */
@RestController
@Slf4j
public class EmpowerController {
    /**
     * Empower授权规则，用来处理请求的来源
     * MyRequestOriginParser类定义授权规则应用属性名
     * 测试使用：http://localhost:8401/empower?serverName=testn
     * @return
     */
    @GetMapping(value = "/empower")
    public String requestSentinel4() {
        log.info("测试Sentinel授权规则empower");
        return "Sentinel授权规则";
    }
}

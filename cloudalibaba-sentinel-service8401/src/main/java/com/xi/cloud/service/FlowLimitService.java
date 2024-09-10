package com.xi.cloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.stereotype.Service;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/10 19:00:12
 * @description
 */
@Service
public class FlowLimitService {

    /**
     * 用于测试流控模式-链路
     */
    @SentinelResource(value = "common")
    public void common() {
        System.out.println("------FlowLimitService come in");
    }
}

package com.xi.cloud.controller;

import com.xi.cloud.service.FlowLimitService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/9 20:59:24
 * @description
 * Sentinel采用懒加载。想使用Sentinel对某个接口进行限流和降级等操作，一定要**先访问下接口，使Sentinel检测出相应的接口
 */
@RestController
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        return "------testB";
    }

    /**
     * 流控-链路演示demo
     * C和D两个请求都访问flowLimitService.common()方法，阈值到达后对C限流，对D不管
     */
    @Resource
    private FlowLimitService flowLimitService;

    @GetMapping("/testC")
    public String testC() {
        flowLimitService.common();
        return "------testC";
    }
    @GetMapping("/testD")
    public String testD() {
        flowLimitService.common();
        return "------testD";
    }

    /**
     * 流控-排队等待演示demo
     * @return
     */
    @GetMapping("/testE")
    public String testE() {
        System.out.println(System.currentTimeMillis()+"      testE,流控效果---排队等待");
        return "------testE";
    }
}

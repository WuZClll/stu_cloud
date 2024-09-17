package com.xi.cloud.controller;

import com.xi.cloud.service.FlowLimitService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/9 20:59:24
 * @description sentinel限流和熔断
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

    /**
     * 新增熔断规则-慢调用比例
     * 10个线程，在一秒的时间内发送完。又因为服务器响应时长设置：暂停1秒，所以响应一个请求的时长都大于1秒综上符合熔断条件，所以当线程开启1秒后，进入熔断状态
     * @return
     */
    @GetMapping("/testF")
    public String testF() {
        //暂停几秒钟线程
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("----测试:新增熔断规则-慢调用比例 ");
        return "------testF 新增熔断规则-慢调用比例";
    }

    /**
     * 新增熔断规则-异常比例
     * @return
     */
    @GetMapping("/testG")
    public String testG() {
        System.out.println("----测试:新增熔断规则-异常比例 ");
        int age = 10/0;
        return "------testG,新增熔断规则-异常比例 ";
    }

    /**
     * 新增熔断规则-异常数
     * @return
     */
    @GetMapping("/testH")
    public String testH() {
        System.out.println("----测试:新增熔断规则-异常数 ");
        int i = ((int) (10 * Math.random())) % 2;// i 为0或1
        int age = 10/i;
        int j = ((int) (10 * Math.random())) % 2;// i 为0或1
        int age1 = 10/i;
        int k = ((int) (10 * Math.random())) % 2;// i 为0或1
        int age2 = 10/i;
        return "------testH,新增熔断规则-异常数 ";
    }
}

package com.xi.cloud.controller;

import cn.hutool.core.util.IdUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/17 23:12:09
 * @description
 */
@RestController
public class PayCircuitController {
    //=========Resilience4j CircuitBreaker 断路器的例子
    @GetMapping(value = "/pay/circuit/{id}")
    public String myCircuit(@PathVariable("id") Integer id) {
        if(id == -4) throw new RuntimeException("----circuit id 不能负数");
        if(id == 9999){
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        }
        return "Hello, circuit(断路器)! inputId:  "+id+" \t " + IdUtil.simpleUUID();
    }

    //=========Resilience4j bulkhead 舱壁隔离的例子
    @GetMapping(value = "/pay/bulkhead/{id}")
    public String myBulkhead(@PathVariable("id") Integer id)
    {
        if(id == -4) throw new RuntimeException("----bulkhead id 不能-4");

        if(id == 9999)
        {
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        }

        return "Hello, bulkhead(舱壁隔离)! inputId:  "+id+" \t " + IdUtil.simpleUUID();
    }

    //=========Resilience4j ratelimit 限流的例子
    @GetMapping(value = "/pay/ratelimit/{id}")
    public String myRatelimit(@PathVariable("id") Integer id)
    {
        return "Hello, myRatelimit限流 欢迎到来 inputId:  "+id+" \t " + IdUtil.simpleUUID();
    }


}
package com.xi.cloud.controller;

import com.xi.cloud.apis.PayFeignApi;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/17 23:36:06
 * @description
 */
@RestController
public class OrderCircuitController {
    @Resource
    private PayFeignApi payFeignApi;

    /**
     * 熔断降级
     * @param id
     * @return
     */
    @GetMapping(value = "/feign/pay/circuit/{id}")
    @CircuitBreaker(name = "cloud-payment-service", fallbackMethod = "myCircuitFallback")// 保险丝注解，name=要调用的微服务名字 fallbackMethod=出问题时兜底的服务降级的方法
    public String myCircuitBreaker(@PathVariable("id") Integer id) {
        // 正常调用 调用远程feign 异常时走fallbackMethod中的方法
        return payFeignApi.myCircuit(id);
    }
    //myCircuitFallback就是服务降级后的兜底处理方法
    public String myCircuitFallback(Integer id,Throwable t) {
        // 这里是容错处理逻辑，返回备用结果
        return "myCircuitFallback (服务降级)，系统繁忙，请稍后再试-----/(ㄒoㄒ)/~~";
    }


//    /**
//     *(船的)舱壁,隔离 type = Bulkhead.Type.SEMAPHORE 信号量舱壁
//     * @param id
//     * @return
//     */
//    @GetMapping(value = "/feign/pay/bulkhead/{id}")
//    @Bulkhead(name = "cloud-payment-service",fallbackMethod = "myBulkheadFallback",type = Bulkhead.Type.SEMAPHORE)// SEMAPHORE：信号量
//    public String myBulkhead(@PathVariable("id") Integer id) {
//        return payFeignApi.myBulkhead(id);
//    }
//    //myBulkheadFallback就是舱壁隔离后的兜底处理方法
//    public String myBulkheadFallback(Throwable t) {
//        return "myBulkheadFallback，隔板超出最大数量限制，系统繁忙，请稍后再试-----/(ㄒoㄒ)/~~";
//    }

    /**
     *(船的)舱壁,隔离 type = Bulkhead.Type.THREADPOOL 固定线程池舱壁
     * ThreadPoolBulkhead只对CompletableFuture方法有效,而CompletableFuture必须新开一个线程
     * @param id
     * @return CompletableFuture
     */
    @GetMapping(value = "/feign/pay/bulkhead/{id}")
    @Bulkhead(name = "cloud-payment-service",fallbackMethod = "myBulkheadPoolFallback",type = Bulkhead.Type.THREADPOOL)// THREADPOOL：线程池
    public CompletableFuture<String> myBulkhead(@PathVariable("id") Integer id) {
        System.out.println(Thread.currentThread().getName()+"\t"+"-----开始进入");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName()+"\t"+"-----准备离开");
        return CompletableFuture.supplyAsync(() -> payFeignApi.myBulkhead(id) + "\t" + "Bulkhead.Type.THREADPOOL固定线程池壁");
    }
    //myBulkheadPoolFallback就是舱壁隔离后的兜底处理方法
    public CompletableFuture<String> myBulkheadPoolFallback(Integer id, Throwable t) {
        return CompletableFuture.supplyAsync(() -> "Bulkhead.Type.THREADPOOL固定线程池壁，系统繁忙，请稍后再试-----/(ㄒoㄒ)/~~");
    }

    /**
     * 限流
     * @param id
     * @return
     */
    @GetMapping(value = "/feign/pay/ratelimit/{id}")
    @RateLimiter(name = "cloud-payment-service",fallbackMethod = "myRatelimitFallback")
    public String myRatelimit(@PathVariable("id") Integer id)
    {
        return payFeignApi.myRatelimit(id);
    }
    public String myRatelimitFallback(Integer id,Throwable t)
    {
        return "你被限流了，禁止访问/(ㄒoㄒ)/~~";
    }




}
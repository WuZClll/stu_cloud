package com.xi.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/17 13:31:29
 * @description 热点规则 自定义限流方法、回调方法
 */
@RestController
@Slf4j
public class RateLimitController {
    /**
     * 按rest地址限流测试
     * @return
     */
    @GetMapping("/rateLimit/byUrl")
    public String byUrl() {
        return "按rest地址限流测试OK";
    }

    /**
     * 按资源名称SentinelResource限流测试
     * @return
     * @SentinelResource(value = "byResourceSentinelResource",blockHandler = "handlerBlockHandler")
     * byResourceSentinelResource资源正常走这个方法，异常（违背了sentinel中的配置）走handlerBlockHandler()
     */
    @GetMapping("/rateLimit/byResource")
    // @SentinelResource,是一个流量防卫防护组件注解，用于指定防护资源，对配置的资源进行流量控制、熔断降级等功能
    @SentinelResource(value = "byResourceSentinelResource",blockHandler = "handlerBlockHandler")
    public String byResource() {
        return "按资源名称SentinelResource限流测试OK";
    }
    public String handlerBlockHandler(BlockException exception) {
        return "服务不可用触发@SentinelResource启动"+"\t"+"o(╥﹏╥)o";
    }

    /**
     * 按SentinelResourcel配置，点击超过限流配置返回自定义限流提示+程序异常返回fallback服务降级
     * @param p1
     * @return
     */
    @GetMapping("/rateLimit/doAction/{p1}")
    // value: 资源名  blockHandler：自定义限流时方法 fallback：兜底的回调方法
    @SentinelResource(value = "doActionSentinelResource",
            blockHandler = "doActionBlockHandler", fallback = "doActionFallback")
    public String doAction(@PathVariable("p1") Integer p1) {
        if (p1 == 0){
            throw new RuntimeException("p1等于零直接异常");
        }
        return "doAction";
    }
    public String doActionBlockHandler(@PathVariable("p1") Integer p1,BlockException e){
        log.error("sentinel配置自定义限流了:{}", e);
        return "sentinel被限流，配置自定义限流了";
    }
    public String doActionFallback(@PathVariable("p1") Integer p1,Throwable e){
        log.error("程序逻辑异常了:{}", e);
        return "程序逻辑异常了"+"\t"+e.getMessage();
    }

    /**
     * 热点参数限流
     * @param p1
     * @param p2
     * @return
     */
    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey",blockHandler = "dealHandler_testHotKey")
    public String testHotKey(@RequestParam(value = "p1",required = false) String p1,
                             @RequestParam(value = "p2",required = false) String p2) {
        return "------testHotKey";
    }
    public String dealHandler_testHotKey(String p1,String p2,BlockException exception) {
        return "-----dealHandler_testHotKey 触发限流";
    }
}

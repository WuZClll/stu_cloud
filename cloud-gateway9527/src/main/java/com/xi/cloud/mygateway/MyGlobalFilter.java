package com.xi.cloud.mygateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/26 20:09:53
 * @description gateway自定义全局过滤器  只要符合yml的gateway断言的任意一个，都会走这个自定义全局过滤器
 * 案例 自定义全局过滤器实现统计接口调用耗时情况
 */
@Component
@Slf4j
public class MyGlobalFilter implements GlobalFilter, Ordered {
    public static final String BEGIN_VISIT_TIME = "begin_visit_time";// 开始调用方法的时间

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 先记录下访问接口的开始时间
        exchange.getAttributes().put(BEGIN_VISIT_TIME, System.currentTimeMillis());// 记录开始时间，放入exchange
        // 2. 返回统计的各个结果给后台
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            Long beginVisitTime = exchange.getAttribute(BEGIN_VISIT_TIME);// 获取开始时间
            if (beginVisitTime != null) {// 有接口访问
                log.info("自定义全局过滤器开始: ###################################################");
                log.info("自定义全局过滤器实现统计接口调用耗时情况--访问接口主机：" + exchange.getRequest().getURI().getHost());
                log.info("自定义全局过滤器实现统计接口调用耗时情况--访问接口端口：" + exchange.getRequest().getURI().getPort());
                log.info("自定义全局过滤器实现统计接口调用耗时情况--访问接口URL：" + exchange.getRequest().getURI().getPath());
                log.info("自定义全局过滤器实现统计接口调用耗时情况--访问接口URL后面的参数：" + exchange.getRequest().getURI().getRawQuery());
                log.info("自定义全局过滤器实现统计接口调用耗时情况--访问接口耗时：" + (System.currentTimeMillis() - beginVisitTime) + "毫秒");
                log.info("自定义全局过滤器结束: ###################################################");
                System.out.println();
            }
        }));
    }

    /**
     * 数字越小优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}

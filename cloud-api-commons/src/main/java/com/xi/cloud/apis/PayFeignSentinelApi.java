package com.xi.cloud.apis;

import com.xi.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author ZC_Wu 汐
 * @date 2024/9/22 14:41
 * @description OpenFeign和Sentinel集成实现fallback服务降级 对外暴露的服务纳入到openfeign里面进行管理 fallback兜底方法统一写到一个类PayFeignSentinelApiFallBack中
 */
@FeignClient(value = "nacos-payment-provider",fallback = PayFeignSentinelApiFallBack.class)
public interface PayFeignSentinelApi {
    /**
     * 对外暴露的方法 该方法对应9001的getPayByOrderNo方法
     * @param orderNo
     * @return
     */
    @GetMapping("/pay/nacos/get/{orderNo}")
    public ResultData getPayByOrderNo(@PathVariable("orderNo") String orderNo);
}

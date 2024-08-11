package com.xi.cloud.controller;

import com.xi.cloud.entities.PayDTO;
import com.xi.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author Wu
 * @date 2024/8/9 16:09:09
 * @description
 */
@RestController
public class OrderController {
//    public static final String PaymentSrv_URL = "http://localhost:8001";// 先写死，硬编码
    public static final String PaymentSrv_URL = "http://cloud-payment-service";// 入驻进spring cloud consul时8001微服务的名字


    @Resource
    private RestTemplate restTemplate;

    /**
     * 添加订单
     * @param payDTO
     * @return
     */
    @GetMapping(value = "/consumer/pay/add")
    public ResultData addOrder(PayDTO payDTO) {
        return restTemplate.postForObject(PaymentSrv_URL + "/pay/add", payDTO, ResultData.class);
    }

    @GetMapping(value = "/consumer/pay/get/{id}")
    public ResultData getPayInfo(@PathVariable("id") Integer id) {
        return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/" + id, ResultData.class, id);
    }

    // 删除、修改 家庭作业
}

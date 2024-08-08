package com.xi.cloud.controller;

import com.xi.cloud.entities.Pay;
import com.xi.cloud.entities.PayDTO;
import com.xi.cloud.service.PayService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Wu
 * @date 2024/8/6 21:13:42
 * @description
 */
@RestController
@Slf4j
public class PayController {
    @Resource
    private PayService payService;

    @PutMapping(value = "/pay/add")
    public String addPay(@RequestBody Pay pay) {
        System.out.println(pay.toString());
        int i = payService.add(pay);
        return "成功插入记录，返回值：" +  i;
    }

    @DeleteMapping(value = "/pay/del/{id}")
    public Integer deletePay(@PathVariable("id") Integer id) {
        return payService.delete(id);
    }

    @PutMapping(value = "/pay/update")
    public String updatePay(@RequestBody PayDTO payDTO) {
        Pay pay = new Pay();
        BeanUtils.copyProperties(payDTO, pay);

        int i = payService.update(pay);
        return "成功返回记录，返回值：" + i;
    }

    @GetMapping(value = "/pay/get/{id}")
    public Pay getById(@PathVariable("id") Integer id) {
        return payService.getById(id);
    }

    @GetMapping(value = "getAll")
    public List<Pay> getAll() {
        return payService.getAll();
    }
}

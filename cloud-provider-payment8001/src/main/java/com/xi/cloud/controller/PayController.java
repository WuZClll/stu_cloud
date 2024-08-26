package com.xi.cloud.controller;

import com.xi.cloud.entities.Pay;
import com.xi.cloud.entities.PayDTO;
import com.xi.cloud.resp.ResultData;
import com.xi.cloud.service.PayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/6 21:13:42
 * @description
 */
@RestController
@Slf4j
@Tag(name = "支付微服务模块",description = "支付CRUD")
public class PayController {
    @Resource
    private PayService payService;

    @PostMapping(value = "/pay/add")
    @Operation(summary = "新增",description = "新增支付流水方法,json串做参数")
    public ResultData<String> addPay(@RequestBody Pay pay) {
        System.out.println(pay.toString());
        int i = payService.add(pay);
        return ResultData.success("成功插入记录，返回值：" +  i);
    }

    @DeleteMapping(value = "/pay/del/{id}")
    @Operation(summary = "删除",description = "删除支付流水方法")
    public ResultData<Integer> deletePay(@PathVariable("id") Integer id) {
        int i = payService.delete(id);
        return ResultData.success(i);
    }

    @PutMapping(value = "/pay/update")
    @Operation(summary = "修改",description = "修改支付流水方法")
    public ResultData<String> updatePay(@RequestBody PayDTO payDTO) {
        Pay pay = new Pay();
        BeanUtils.copyProperties(payDTO, pay);

        int i = payService.update(pay);
        return ResultData.success("成功修改记录，返回值：" + i);
    }

    @GetMapping(value = "/pay/get/{id}")
    @Operation(summary = "按照ID查流水", description = "查询支付流水方法")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        Pay pay = payService.getById(id);
        try {
            // 暂停62秒钟线程 测试OpenFeign的默认超时时间
            TimeUnit.SECONDS.sleep(62);// 测出OpenFeign默认超时时间60s
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResultData.success(pay);
    }

    @GetMapping(value = "getAll")
    @Operation(summary = "查所有流水",description = "查询所有支付流水方法")
    public ResultData<List<Pay>> getAll() {
        return ResultData.success(payService.getAll());
    }


    // 测试consul上的配置本地是否可以获取成功
    @Value("${server.port}")
    private String port;
    @GetMapping(value = "/pay/get/info")
    public String getInfoByConsul(@Value("${xi.info}") String xiInfo) {
        return "xiInfo: " + xiInfo + "\t" + "port: " + port;

    }
}

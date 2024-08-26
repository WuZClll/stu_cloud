package com.xi.cloud.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.xi.cloud.entities.Pay;
import com.xi.cloud.resp.ResultData;
import com.xi.cloud.service.PayService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/22 10:37:38
 * @description gateway网关
 */
@RestController
public class PayGateWayController {
    @Resource
    PayService payService;

    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        Pay pay = payService.getById(id);
        return ResultData.success(pay);
    }

    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo() {
        return ResultData.success("gateway info test："+ IdUtil.simpleUUID());
    }

    /**
     * 测试filter过滤器
     * @param request
     * @return
     */
    @GetMapping(value = "/pay/gateway/filter")
    public ResultData<String> getGatewayFilter(HttpServletRequest request) {
        System.out.println("=========请求头(RequestHeader)相关filter=========");
        String result = "";
        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()) {
            String headName = headers.nextElement();
            String headValue = request.getHeader(headName);
            System.out.println("请求头名: " + headName +"\t\t\t"+"请求头值: " + headValue);
            if(headName.equalsIgnoreCase("X-Request-xi1")
                    || headName.equalsIgnoreCase("X-Request-xi2")) {
                result = result+headName + "\t " + headValue +" ";
            }
        }

        System.out.println("=========请求参数(RequestParameter)相关filter=========");
        String customerId = request.getParameter("customerId");
        String customerName = request.getParameter("customerName");
        System.out.println("request Parameter customerId: "+customerId);
        System.out.println("request Parameter customerName: "+customerName);

        System.out.println("=============================================");
        return ResultData.success("getGatewayFilter 过滤器 test： "+result+" \t "+ DateUtil.now());
    }
}
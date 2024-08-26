package com.xi.cloud.mygateway;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/23 15:12:10
 * @description gateway自定义断言
 * 1. 新建类名XXX需要以RoutePredicateFactory结尾并继承AbstractRoutePredicateFactory类
 * 2. 重写apply方法
 * 3. 新建appIy方法所需要的静态内部类RoutePredicateFactory.Config**这个Config类就是我们的路由断言规则，重要**
 * 4. 空参构造方法，内部调用super
 * 5. 重写apply方法第二版
 * 需求说明：自定义配置会员等级userType，按照钻、金、银和yml配置的会员等级，以适配是否可以访问
 */
@Component
// 1. 类名必须以RoutePredicateFactory结尾并继承AbstractRoutePredicateFactory类
public class MyRoutePredicateFactory extends AbstractRoutePredicateFactory<MyRoutePredicateFactory.Config> {

    // 4. 空参构造方法，内部调用super
    public MyRoutePredicateFactory() {
        super(MyRoutePredicateFactory.Config.class);
    }

    // 3. 新建appIy方法所需要的静态内部类RoutePredicateFactory.Config 这个Config类就是我们的路由断言规则，重要
    @Validated
    public static class Config {
        @Setter
        @Getter
        @NotEmpty
        private String userType;// 用户类型

    }

    // 2. 重写apply方法
    @Override
    public Predicate<ServerWebExchange> apply(MyRoutePredicateFactory.Config config) {
        return new Predicate<ServerWebExchange>() {
            @Override
            public boolean test(ServerWebExchange serverWebExchange) {
                // 检查request的参数里面，userType是否为指定的值，符合配置就通过
                // http://localhost:9527/pay/gateway/get/1?userType=diamond 进行测试 // 钻石用户允许访问  yml中配置 `- My=diamond` # 自定义断言 diamond钻石用户可以访问
                String userType = serverWebExchange.getRequest().getQueryParams().getFirst("userType");// 获取路径参数userType的值
                if (userType == null) {
                    return false;
                }
                // 如果说参数存在，就和config的数据进行比较
                if (userType.equalsIgnoreCase(config.getUserType())) {// 忽略大小写比对
                    return true;
                }
                return false;
            }
        };
    }

    // yml短格式配置 不重写这个方法只能使用满格式写
    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("userType");
    }
}

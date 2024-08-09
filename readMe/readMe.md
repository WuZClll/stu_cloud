### 运行环境要求
| 组件           | 版本           | 
|---------------|--------------|
| java          | java17+      |
| cloud         | 2023.0.0     |
| boot          | 3.2.0        |
| cloud alibaba | 2022.0.0-RC2 |
| Maven         | 3.9+         |
| Mysql         | 8.0+         |

[课程笔记](./note.html)

## 一. cloud-provider-payment8001

微服务提供者支付module模块

### 微服务小口诀

建module->改pom->写yml->主启动->业务类

### Swagger3生成接口文档

| 注解           | 标注位置          | 作用                   |
| :------------- | :---------------- | :--------------------- |
| @**Tag**       | controller类      | 标识controller作用     |
| @Parameter     | 参数              | 标识参数的作用         |
| @Parameters    | 参数              | 标识参数的作用         |
| @**Schema**    | model层的JavaBean | 描述模型作用及每个属性 |
| @**Operation** | 方法              | 描述方法的作用         |
| @ApiResponse   | 方法              | 描述响应状态码等       |

含分组迭代的Config配置类
```java
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class Swagger3Config {
    @Bean
    public GroupedOpenApi PayApi()
    {
        return GroupedOpenApi.builder().group("支付微服务模块").pathsToMatch("/pay/**").build();
    }
    @Bean
    public GroupedOpenApi OtherApi()
    {
        return GroupedOpenApi.builder().group("其它微服务模块").pathsToMatch("/other/**", "/others").build();
    }
    @Bean
    public GroupedOpenApi CustomerApi()
    {
        return GroupedOpenApi.builder().group("客户微服务模块").pathsToMatch("/customer/**", "/customers").build();
    }

    @Bean
    public OpenAPI docsOpenApi()
    {
        return new OpenAPI()
                .info(new Info().title("cloud2024")
                        .description("通用设计rest")
                        .version("v1.0"))
                .externalDocs(new ExternalDocumentation()
                        .description("www.atguigu.com")
                        .url("https://yiyan.baidu.com/"));
    }
}
```

调用方式：[生成的swagger地址](http://localhost:8001/swagger-ui/index.html)
http://localhost:8001/swagger-ui/index.html

## 二. cloud-consumer-order80

### RestTemplate访问远程Http

RestTemplate提供了多种便捷**访问远程Http**服务的方法， 是一种简单便捷的访问restful服务模板类，是Spring提供的用于访问Rest服务的客户端模板工具集

[RestTemplate官网](https://docs.spring.io/spring-framework/docs/6.0.11/javadoc-api/org/springframework/web/client/RestTemplate.html)

使用restTemplate访问restful接口非常的简单粗暴无脑。

(url, requestMap, ResponseBean.class)这三个参数分别代表：REST请求地址、请求参数、HTTP响应转换被转换成的对象类型。

![image-20240809155810228](./MDImg/image-20240809155810228.png)

getForObject: 返回对象为响应体中数据转化成的对象，基本上可以理解为Json

getForEntity: 返回对象为ResponseEntity对象，包含了响应中的一些重要信息，比如响应头、响应状态码、响应体等

**get请求方法**

```java
<T> T getForObject(String url, Class<T> responseType, Object... uriVariables);

<T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables);

<T> T getForObject(URI url, Class<T> responseType);

<T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables);

<T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables);

<T> ResponseEntity<T> getForEntity(URI var1, Class<T> responseType);
```

**post请求方法**

```java
<T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables);

<T> T postForObject(String url, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables);

<T> T postForObject(URI url, @Nullable Object request, Class<T> responseType);

<T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType, Object... uriVariables);

<T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType, Map<String, ?> uriVariables);

<T> ResponseEntity<T> postForEntity(URI url, @Nullable Object request, Class<T> responseType);
```


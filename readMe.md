原视频：

<iframe src="//player.bilibili.com/player.html?isOutside=true&aid=1851137936&bvid=BV1gW421P7RD&cid=1450576283&p=1&autoplay=0" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen="true"></iframe>

# 运行环境要求

| 组件          | 版本         |
| ------------- | ------------ |
| java          | java17+      |
| cloud         | 2023.0.0     |
| boot          | 3.2.0        |
| cloud alibaba | 2022.0.0-RC2 |
| Maven         | 3.9+         |
| Mysql         | 8.0+         |

[前往尚硅谷课程笔记](./note.html)

| 模块                                                         | 用途                                                |
| :----------------------------------------------------------- | :-------------------------------------------------- |
| [cloud-provider-payment8001](##一. cloud-provider-payment8001) | 微服务提供者支付module模块8001                      |
| cloud-provider-payment8002                                   | 微服务提供者支付module模块8002                      |
| [cloud-consumer-order80](##二. cloud-consumer-order80)       | 微服务调用者订单module模块-使用LoadBalancer负载均衡 |
| [cloud-consumer-feign-order80](#Ⅳ. OpenFeign服务接口调用)    | 微服务调用者订单module模块-使用OpenFeign负载均衡    |
| [cloud-api-commons](##三. cloud-api-commons)                 | 对外暴露通用的组件/api/接口/工具类等                |

![image-20240811192237239](./MDImg/image-20240811192237239.png)

# Ⅰ. 微服务架构Base工程模块构建

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

微服务调用者订单module模块

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

### 硬编码写死问题

```java
public static final String PaymentSrv_URL = "http://localhost:8001";// 硬编码
```

[解决方式](##硬编码写死问题解决)

## 三. cloud-api-commons

对外暴露通用的组件/api/接口/工具类等

### 工程重构

发现系统中有重复部分，进行**工程重构**，将重复部分写入cloud-api-commons，将其作为对外暴露通用的组件/api/接口/工具类

对cloud-api-commons执行maven命令`clear`、`install`

```html
<!-- 对需要使用cloud-api-commons对外暴露的通用组件/api/接口/工具类的微服务引入自己定义的api通用包 -->
<dependency>
    <groupId>com.xi.cloud</groupId>
    <artifactId>cloud-api-commons</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### 开始引入SpringCloud

微服务所在的IP地址和端口号硬编码到订单微服务中，会存在非常多的问题

1. 如果订单微服务和支付微服务的IP地址或者端口号发生了变化，则支付微服务将变得不可用，需要同步修改订单微服务中调用支付微服务的IP地址和端口号。
2. 如果系统中提供了多个订单微服务和支付微服务，则无法实现微服务的负载均衡功能。
3. 如果系统需要支持更高的并发，需要部署更多的订单微服务和支付微服务，硬编码订单微服务则后续的维护会变得异常复杂。

所以，在微服务开发的过程中，需要引入服务治理功能，实现微服务之间的动态注册与发现，从此刻开始我们**正式进入SpringCloud实战**

# Ⅱ. Consul服务注册与发现

微服务所在的IP地址和端口号硬编码到订单微服务中，会存在非常多的问题

1. 如果订单微服务和支付微服务的IP地址或者端口号发生了变化，则支付微服务将变得不可用，需要同步修改订单微服务中调用支付微服务的IP地址和端口号。
2. 如果系统中提供了多个订单微服务和支付微服务，则无法实现微服务的负载均衡功能。
3. 如果系统需要支持更高的并发，需要部署更多的订单微服务和支付微服务，硬编码订单微服务则后续的维护会变得异常复杂。

所以，在微服务开发的过程中，需要引入服务治理功能，实现微服务之间的动态注册与发现，从此刻开始我们正式进入SpringCloud实战

## consul 简介

[consul官网](https://developer.hashicorp.com/consul/docs)

Consul 是一套开源的**分布式服务发现和配置管理系统**，由 HashiCorp 公司用 Go 语言开发。

提供了微服务系统中的服务治理、配置中心、控制总线等功能。这些功能中的每一个都可以根据需要单独使用，也可以一起使用以构建全方位的服务网格，总之Consul提供了一种完整的服务网格解决方案。它具有很多优点。包括： 基于 raft 协议，比较简洁； 支持健康检查, 同时支持 HTTP 和 DNS 协议 支持跨数据中心的 WAN 集群 提供图形界面 跨平台，支持 Linux、Mac、Windows

[Spring Cloud consul官网](https://spring.io/projects/spring-cloud-consul)

Spring Cloud Consul 功能：

- 服务发现：提供HTTP和DNS两种发现方式
- 健康监测：支持多种方式，HTTP、TCP、Docker、Shell脚本定制
- KV存储：Key、Value的存储方式
- 多数据中心：Consul支持多数据中心

## consul使用

下载consul[Install | Consul | HashiCorp Developer](https://developer.hashicorp.com/consul/install#windows)

安装路径下`consul agent -dev`以开发模式启动

通过http://localhost:8500可以访问Consul的首页

**步骤**

1. pom引入坐标(下方有优化)

   ```html
   <!--SpringCloud consul discovery -->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-consul-discovery</artifactId>
   </dependency>
   ```

2. yml
   ```yaml
   spring:
     application:
       name: ???
     ####Spring Cloud Consul for Service Discovery(服务发现)
     cloud:
       consul:
         # 要入驻进的是哪个consul
         host: localhost
         port: 8500
         discovery:
           # 此模块将要以这个名字入驻进Consul
           service-name: ${spring.application.name}
   ```

3. 主启动
   `@EnableDiscoveryClient`: 放在启动类上， 开启服务发现

**启动时出现警告**

启动此服务控制台出现`Standard Commons Logging discovery in action with spring-jcl: please remove commons-logging.jar from classpath in order to avoid potential conflicts`提示，即“使用spring-jcl进行标准Commons日志发现:请从类路径中删除Commons - Logging .jar，以避免潜在的冲突”，可以正常运行，有代码洁癖可以使用以下方法解决

```html
<!--SpringCloud consul discovery -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-consul-discovery</artifactId>
    <!-- 排除Commons-Logging.jar，以避免潜在的冲突 -->
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## 硬编码写死问题解决

**原问题**:

```java
public static final String PaymentSrv_URL = "http://localhost:8001";// 硬编码
```

**解决**：

```java
public static final String PaymentSrv_URL = "http://cloud-payment-service";// 入驻进spring cloud consul时8001微服务的名字
```

**但由此引发另一个问题**：`Caused by: java.net.UnknownHostException: cloud-payment-service`，如果通过微服务名称调用，consul默认有负载均衡，后面是多个(一个集群)，因此需要告诉[RestTemplate](###RestTemplate访问远程Http)(远程访问Http)要支持负载均衡

**解决**：
修改RestTemplate的配置类，在构造方法上加`@LoadBalanced`注解，提供RestTemplate(远程访问Http)对负载均衡的支撑

```java
@Configuration// 使用 @Configuration 标记类作为配置类替换 xml 配置文件
public class RestTemplateConfig {
    @Bean
    @LoadBalanced// 提供RestTemplate(远程访问Http)对负载均衡的支撑
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

## 三个注册中心异同点

| 组件名    | 语言 | CAP  | 服务健康检查 | 对外暴露接口 | SpriongCloud集成 |
| --------- | ---- | ---- | ------------ | ------------ | ---------------- |
| Eureka    | Java | AP   | 可配支持     | HTTP         | 已集成           |
| Consul    | Go   | CP   | 支持         | HTTP/DNS     | 已集成           |
| Zookeeper | Java | CP   | 支持         | 客户端       | 已集成           |

**CAP**

- C: Consistency（强一致性）
- A: Avaliblity（可用性）
- P: Partition tolerance（分区容错性）

**经典CAP图**

最多只能同时较好的满足两个。

 **CAP理论的核心**：一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性这三个需求，

因此，根据 CAP 原理将 NoSQL 数据库分成了满足 CA 原则、满足 CP 原则和满足 AP 原则三 大类：

- CA - 单点集群，满足一致性，可用性的系统，通常在可扩展性上不太强大。
- CP - 满足一致性，分区容忍必的系统，通常性能不是特别高。
- AP - 满足可用性，分区容忍性的系统，通常可能对一致性要求低一些。

![image-20240811170229471](./MDImg/image-20240811170229471.png)

### AP架构

![image-20240811170637990](./MDImg/image-20240811170637990.png)

如Eureka，当网络分区出现后，为了保证可用性，系统B**可以返回旧值**，保证系统的可用性。

当数据出现不一致时，虽然A, B上的注册信息不完全相同，但每个Eureka节点依然能够正常对外提供服务，这会出现查询服务信息时如果请求A查不到，但请求B就能查到。如此保证了可用性但牺牲了一致性

**结论**：**违背了一致性**C的要求，只**满足可用性和分区容错**，即AP

### CP架构

![image-20240811171111998](./MDImg/image-20240811171111998.png)

如Zookeeper/Consul，当网络分区出现后，为了保证一致性，**就必须拒接请求**，否则无法保证一致性

Consul 遵循CAP原理中的CP原则，保证了强一致性和分区容错性，且使用的是Raft算法，比zookeeper使用的Paxos算法更加简单。虽然保证了强一致性，但是可用性就相应下降了，例如服务注册的时间会稍长一些，因为 Consul 的 raft 协议要求**必须过半数的节点都写入成功才认为注册成功 ；在leader挂掉了之后，重新选举出leader之前会导致Consul 服务不可用**。

**结论**：**违背了可用性**A的要求，只**满足一致性和分区容错**，即CP

## Consul服务的配置与刷新

微服务意味着要将单体应用中的业务拆分成一个个子服务，每个服务的粒度相对较小，因此系统中会出现**大量的服务**。由于**每个服务都需要必要的配置信息**才能运行，**所以一套集中式的、动态的配置管理设施是必不可少的**。

比如某些配置文件中的内容大部分都是相同的，只有个别的配置项不同。就拿数据库配置来说吧，如果每个微服务使用的技术栈都是相同的，则每个微服务中关于数据库的配置几乎都是相同的，有时候主机迁移了，**我希望一次修改，处处生效**。

当下我们每一个微服务自己带着一个application.yml，上百个配置文件的管理....../(ㄒoㄒ)/~~

### 服务配置**步骤**

**需求**

通过全局配置信息，直接注册进Consul服务器，从Consul获取，因此要遵守Consul的配置规则要求

 **步骤**

1. pom
   ```html
   <!--SpringCloud consul config consul配置管理-->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-consul-config</artifactId>
   </dependency>
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-bootstrap</artifactId>
   </dependency>
   ```

2. yml
   [yml配置规则说明](####yml配置规则说明)

   新增bootstrop.yml

   ```yaml
   # 系统级配置
   spring:
     application:
       name: XXX
       ####Spring Cloud Consul for Service Discovery
     cloud:
       consul:
         host: localhost
         port: 8500
         discovery:
           service-name: ${spring.application.name}
         config:
           profile-separator: '-' # 官方默认分隔符为 ","，我们把它更新为 '-'
           format: YAML
   ```

   修改application.yml
   ```yaml
   # 用户级配置
   server:
     port: 8001
   
   # ==========applicationName + druid-mysql8 driver===================
   spring:
     datasource:
       type: com.alibaba.druid.pool.DruidDataSource
       driver-class-name: com.mysql.cj.jdbc.Driver
       url: jdbc:mysql://localhost:3306/db_name?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8&rewriteBatchedStatements=true&allowPublicKeyRetrieval=true
       username: root
       password: password
     profiles:
       active: dev # 多环境配置加载内容dev/prod,不写就是默认default配置
   
   # ========================mybatis===================
   mybatis:
     mapper-locations: classpath:mapper/*.xml
     type-aliases-package: com.xi.cloud.entities
     configuration:
       map-underscore-to-camel-case: true
   ```

3. [Consul服务器key/value配置填写](####Consul服务器key/value配置填写)
   创建文件

   - config/分布式微服务名字/data
   - ​             /分布式微服务名字-dev/data
   - ​             /分布式微服务名字-prod/data

   

4. 

   

#### yml配置规则说明

![image-20240811173433996](./MDImg/image-20240811173433996.png)

新增配置文件bootstrop.yml

- bootstrop.yml是什么？
  applicaiton.yml是**用户级**的资源配置项
  bootstrap.yml是**系统级**的，**优先级更加高**

  Spring Cloud会创建一个“Bootstrap Context”，作为Spring应用的`Application Context`的**父上下文**。初始化的时候，`Bootstrap Context`负责从外部源加载配置属性并解析配置。这两个上下文共享一个从外部获取的`Environment`。

  `Bootstrap`属性有高优先级，默认情况下，它们不会被本地配置覆盖。 `Bootstrap context`和`Application Context`有着不同的约定，所以新增了一个`bootstrap.yml`文件，保证`Bootstrap Context`和`Application Context`配置的分离。

   **application.yml文件改为bootstrap.yml,这是很关键的或者两者共存**

  因为bootstrap.yml是比application.yml先加载的。bootstrap.yml优先级高于application.yml

#### Consul服务器key/value配置填写

参考规则

```yaml
# config/分布式微服务名字/data # 不写就是默认default配置
#       /分布式微服务名字-dev/data
#       /分布式微服务名字-prod/data
```

1. 创建config文件夹，以/结尾

   ![image-20240811180326427](./MDImg/image-20240811180326427.png)

2. config文件下分别创建三个文件夹，以/结尾
   由于我们配置了`spring: cloud: consul: config: profile-separator: '-' # 官方默认分隔符为 ","，我们把它更新为 '-'`
   所以在config文件夹下创建`分布式微服务名字`、`分布式微服务名字-dev`、`分布式微服务名字-prod`三个文件夹，以/结尾
   ![image-20240811181021798](./MDImg/image-20240811181021798.png)

3. 第二步创建的三个文件夹下分别创建data文件(可写入内容)，data不再是文件夹
   ![image-20240811182008711](./MDImg/image-20240811182008711.png)

### 动态刷新步骤

主启动类上加上`@RefreshScope`注解，可以在服务器配置变更时自动刷新 默认间隔为55s后自动刷新

可修改配置文件`spring.cloud.consul.config.watch.tait-time: ?`?为秒数，修改间隔时间

## 引出问题

Consul重启后consul里的配置消失了

由此我们需要做Consul的配置持久化

[LoadBalancer解决Consul持久化](###LoadBalancer之Consul持久化)

# Ⅲ.LoadBalancer负载均衡服务调用

前身是Ribbon目前已进入维护模式

Spring Cloud Ribbon是基于Netflix Ribbon实现的一套**客户端    负载均衡**的工具。

简单的说，Ribbon是Netflix发布的开源项目，主要功能是**提供客户端的软件负载均衡算法和服务调用**。Ribbon客户端组件提供一系列完善的配置项如连接超时，重试等。简单的说，就是在配置文件中列出Load Balancer（简称LB）后面所有的机器，Ribbon会自动的帮助你基于某种规则（如简单轮询，随机连接等）去连接这些机器。我们很容易使用Ribbon实现自定义的负载均衡算法。

Ribbon未来替换方案：**spring-cloud-loadbalancer**

[spring-cloud-loadbalancer官网](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/#spring-cloud-loadbalancer)

## spring-cloud-loadbalancer是什么

**LB负载均衡(Load Balance)是什么**

简单的说就是将用户的请求平摊的分配到多个服务上，从而达到系统的HA（高可用），常见的负载均衡有软件Nginx，LVS，硬件 F5等

**spring-cloud-starter-loadbalancer组件是什么**

Spring Cloud LoadBalancer是由SpringCloud官方提供的一个开源的、简单易用的**客户端负载均衡器**，它包含在SpringCloud-commons中**用它来替换了以前的Ribbon组件**。相比较于Ribbon，SpringCloud LoadBalancer不仅能够支持RestTemplate，还支持WebClient（WeClient是Spring Web Flux中提供的功能，可以实现响应式异步请求）

## loadBalancer负载均衡

### 客户端和服务器端负载均衡的区别

loadbalancer本地负载均衡客户端和Nginx服务端负载均衡区别

- Nginx是**服务器负载均衡**，客户端所有请求都会交给nginx，然后由**nginx实现转发请求**，即负载均衡是由服务端实现的。
- loadbalancer**本地负载均衡**，在调用微服务接口时候，会在**注册中心上获取注册信息服务列表之后缓存到JVM本地**，从而在**本地实现RPC远程服务调用技术**。

### 负载均衡案例

#### 理论

80通过轮询负载访问8001、8002、8003

![image-20240811194424109](./MDImg/image-20240811194424109.png)

LoadBalancer 在工作时分成两步：

- **第一步**，先选择ConsulServer从服务端查询并拉取服务列表，知道了它有多个服务(上图3个服务)，这3个实现是完全一样的，默认轮询调用谁都可以正常执行。类似生活中求医挂号，某个科室今日出诊的全部医生，客户端你自己选一个。
- **第二步**，按照指定的负载均衡策略从server取到的服务注册列表中由客户端自己选择一个地址，所以LoadBalancer是一个**客户端的**负载均衡器。

#### 实操

1. 启动Consul，将8001、8002启动后注册进微服务

2. 订单80模块(客户端，消费者侧)**修改POM**并注册进consul**新增LoadBalancer组件**
   ```html
   <!--loadbalancer-->
   <dependency>
       <groupId>org.springframework.cloud</groupId>
       <artifactId>spring-cloud-starter-loadbalancer</artifactId>
   </dependency>
   ```

3. 修改Controller进行测试

   ```java
   @GetMapping(value = "/consumer/pay/get/info")
   private String getInfoByConsul() {
       // PaymentSrv_URL是注册进consul中的微服务名字(同一微服务名可能有多个不同端口的相同微服务)
       return restTemplate.getForObject(PaymentSrv_URL + "/pay/get/info", String.class);
   }
   ```

4. 发现交替访问8001、8002

### 负载均衡算法

默认**轮询**： $rest接口第几次请求数 \% 服务器集群总数量 = 实际调用服务器位置下标$ ，每次服务重启动后rest接口计数从1开始。

`List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");`

如：` List [0] instances = 127.0.0.1:8002`

　　`　List [1] instances = 127.0.0.1:8001`

8001+ 8002 组合成为集群，它们共计2台机器，集群总数为2， 按照**轮询算法**原理：

- 当总请求数为1时： 1 % 2 =1 对应下标位置为1 ，则获得服务地址为127.0.0.1:8001
- 当总请求数位2时： 2 % 2 =0 对应下标位置为0 ，则获得服务地址为127.0.0.1:8002
- 当总请求数位3时： 3 % 2 =1 对应下标位置为1 ，则获得服务地址为127.0.0.1:8001
- 当总请求数位4时： 4 % 2 =0 对应下标位置为0 ，则获得服务地址为127.0.0.1:8002
- 如此类推......

默认有两种算法 轮询和随机，一般来说轮询就够用了

**修改负载均衡算法**

```java
@Configuration// 使用 @Configuration 标记类作为配置类替换 xml 配置文件
@LoadBalancerClient(value = "cloud-payment-service",configuration = RestTemplateConfig.class)// @LoadBalancerClient 将要对value这个微服务执行实现新的RestTemplate配置 这个配置在configuration这个类中      value值大小写一定要和consul里面的名字一样，必须一样       不使用这个注解默认为轮询
public class RestTemplateConfig {
    @Bean
    @LoadBalanced //使用@LoadBalanced注解赋予RestTemplate负载均衡的能力 提供RestTemplate(远程访问Http)对负载均衡的支撑
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /**
     * 默认轮询 加此方法，修改为了随机
     * @param environment
     * @param loadBalancerClientFactory
     * @return
     */
    @Bean    ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
                                                           LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);

        return new RandomLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
    }
}
```

## LoadBalancer之Consul持久化

Consul数据持久化配置，并且注册为Windows服务

1. consul下载目录下新建空文件夹`mydata`和`consul_start.bat`文件

2. `consul_start.bat`内容信息

   ```bash
   @echo.服务启动......  
   @echo off  
   @sc create Consul binpath= "D:\devSoft\consul_1.17.0_windows_386\consul.exe agent -server -ui -bind=127.0.0.1 -client=0.0.0.0 -bootstrap-expect  1  -data-dir D:\devSoft\consul_1.17.0_windows_386\mydata   "
   @net start Consul
   @sc config Consul start= AUTO  
   @echo.Consul start is OK......success
   @pause
   ```

   - `@sc create Consul binpath= "Consul安装路径\consul.exe agent -server(以服务器后台形式启动) -ui -bind=127.0.0.1(绑定本机) -client=0.0.0.0 -bootstrap-expect  1  -data-dir consul的配置数据存储路径"`
   - `@sc config Consul start= AUTO (是否每次开机启动) `

3. 右键管理员权限打开

4. 后续consul的配置数据会保存进mydata文件夹

这样每次开机都会自动启动consul服务

# Ⅳ. OpenFeign服务接口调用

## Spring Cloud OpenFeign

**OpenFeign默认集成了LoadBalancer负载均衡功能**

Feign是一个**声明性web服务客户端**。它使编写web服务客户端变得更容易。**使用Feign创建一个接口并对其进行注释**。它具有可插入的注释支持，包括Feign注释和JAX-RS注释。Feign还支持可插拔编码器和解码器。Spring Cloud添加了对Spring MVC注释的支持，以及对使用Spring Web中默认使用的HttpMessageConverter的支持。Spring Cloud集成了Eureka、Spring Cloud CircuitBreaker以及Spring Cloud LoadBalancer，以便在使用Feign时提供负载平衡的http客户端。

**OpenFeign是一个声明式的Web服务客户端，只需创建一个Rest接口，并在该接口上添加注解`@FeignClient`即可**

OpenFeign基本上就是当前微服务之间调用的事实标准

**OpenFeign功能**

1. 可插拔的注解支持，包括Feign注解和JAX-RS注解
2. 支持可插拔的HTTP编码器和解码器
3. 支持Sentinel和它的Fallback
4. 支持SpringCloudLoadBalancer的负载均衡
5. 支持HTTP请求和响应的压缩

前面在使用**SpringCloud LoadBalancer**+RestTemplate时，利用RestTemplate对http请求的封装处理形成了一套模版化的调用方法。

**但是在实际开发中**，由于对服务依赖的调用可能不止一处，**往往一个接口会被多处调用，所以通常都会针对每个微服务自行封装一些客户端类来包装这些依赖服务的调用**。所以，OpenFeign在此基础上做了进一步封装，由他来帮助我们定义和实现依赖服务接口的定义。在OpenFeign的实现下，**我们只需创建一个接口并使用注解的方式来配置它(在一个微服务接口上面标注一个`@FeignClient`注解即可)**，即可完成对服务提供方的接口绑定，统一对外暴露可以被调用的接口方法，大大简化和降低了调用客户端的开发量，也即由服务提供者给出调用接口清单，消费者直接通过OpenFeign调用即可。

**OpenFeign同时还集成SpringCloud LoadBalancer**
可以在使用OpenFeign时提供Http客户端的负载均衡，也可以集成阿里巴巴Sentinel来提供熔断、降级等功能。而与SpringCloud LoadBalancer不同的是，**通过OpenFeign只需要定义服务绑定接口且以声明式的方法**，优雅而简单的实现了服务调用。

## OpenFeign通用步骤

OpenFeign默认集成了LoadBalancer负载均衡功能

微服务Api接口+`@FeignClient`注解标签

![image-20240812181945926](./MDImg/image-20240812181945926.png)

服务消费者80 → 调用含有@FeignClient注解的Api服务接口 → 服务提供者(8001/8002)

**流程步骤**

修改客户端

- pom

  ```html
  <!--openfeign-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>
  ```

- yml
  ```yaml
  server:
    port: XX
  spring:
    application:
      name: XXX
    ####Spring Cloud Consul for Service Discovery(服务发现)
    cloud:
      consul:
        host: localhost
        port: 8500
        discovery:
          # 80将要以这个名字入驻进Consul
          prefer-agent-address: true # 是否优先使用ip进行注册
          service-name: ${spring.application.name}
  ```

- 启动类

  ```java
  @SpringBootApplication
  @EnableDiscoveryClient // 该注解用于向使用consul为注册中心时注册服务
  @EnableFeignClients// 启用feign客户端,定义服务+绑定接口，以声明式的方法优雅而简单的实现服务调用
  ```

业务类
订单模块要去调用支付模块，订单和支付两个微服务，需要通过Api接口解耦，一般不要在订单模块写非订单相关的业务。
自己的业务自己做 + 其它模块走FeignApi接口调用

**修改对外暴露的通用模块**

- 引入pom
  ```html
  <!--openfeign-->
  <dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-openfeign</artifactId>
  </dependency>
  ```

- 新建服务接口`PayFeignApi`，头上配置`@FeignClient`注解
  ```java
  @FeignClient("注册进consul中的微服务名")
  public interface PayFeignApi
  ```

- 参考微服务8001的Controller层，新建PayFeignApi中的接口
  ![image-20240812201008741](./MDImg/image-20240812201008741.png)

- Controller
  ```java
  @RestController
  @Slf4j
  public class OrderController
  {
      @Resource
      private PayFeignApi payFeignApi;
  
      @PostMapping("/feign/pay/add")
      public ResultData addOrder(@RequestBody PayDTO payDTO) {
          System.out.println("第一步：模拟本地addOrder新增订单成功(省略sql操作)，第二步：再开启addPay支付微服务远程调用");
          ResultData resultData = payFeignApi.addPay(payDTO);
          return resultData;
      }
  }
  ```

## OpenFeign高级特性

配置类:

```java
@Configuration
public class FeignConfig {

    /**
     * OpenFeign配置重试机制bean
     * @return
     */
    @Bean
    public Retryer myRetryer() {
//        return Retryer.NEVER_RETRY; //Feign默认配置是不走重试策略的

        //修改最大请求次数为3(1初始次数+2重复次数)，初始间隔时间为100ms，重试间最大间隔时间为1s
        return new Retryer.Default(100,1,3);
    }

    /**
     * OpenFeign配置日志bean
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
```

yml:

```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
#          default: # 全局通用配置
#            #连接超时时间
#            connectTimeout: 3000
#            #读取超时时间
#            readTimeout: 3000
          cloud-payment-service: # 全局通用配置
            #连接超时时间
            connectTimeout: 3000
            #读取超时时间
            readTimeout: 3000
      httpclient:
        hc5:
          enabled: true # 启用httpclient5提升性能. OpenFeign使用 Apache HttpClient 5 替换OpenFeign默认的HttpURLConnection 以提升性能
      compression: # 请求回应压缩
        request: # 请求
          enabled: true # 开启请求压缩功能
          min-request-size: 2048 # 最小触发压缩的大小
          mime-types: text/xml,application/xml,application/json # 触发压缩数据类型
        response: # 响应
          enabled: true # 开启响应压缩功能

# logging.level + 含有@FeignClient注解的完整带包名的接口名+debug级别
# feign日志以什么级别监控哪个接口(OpenFeign对外暴露服务功能清单的接口)
logging:
  level:
    com:
      xi:
        cloud:
          apis:
            # 日志级别:
            #- `NONE`：默认的，不显示任何日志；
            #- `BASIC`：仅记录请求方法、URL、响应状态码及执行时间；
            #- `HEADERS`：除了 `BASIC` 中定义的信息之外，还有请求和响应的头信息；
            #- `FULL`：除了 `HEADERS` 中定义的信息之外，还有请求和响应的正文及元数据。
            PayFeignApi: debug
```



### OpenFeign之超时控制

默认OpenFeign客户端等待60秒钟，但是服务端处理超过规定时间会导致Feign客户端返回报错。

为了避免这样的情况，有时候我们需要设置Feign客户端的超时控制，默认60秒太长或者业务时间太短都不好

**yml文件中开启配置**：

全局配置方式：

```yaml
spring:
 cloud:
  openfeign:
   client:
    config:
     default: # 全局通用配置
      connectTimeout: ??? # 连接超时时间
      readTimeout: ??? # 请求处理超时时间
```

指定配置方式：

```yaml
spring:
 cloud:
  openfeign:
   client:
    config:
     cloud-payment-service: # 调用此微服务的配置
      connectTimeout: ??? # 连接超时时间
      readTimeout: ??? # 请求处理超时时间
```

注：当全局和指定配置都有时，以更细致(指定)的为准

### OpenFeign之重试机制

默认重试机制是关闭的，给了默认值。

默认情况下会创建Retryer.NEVER_RETRY类型为Retryer的bean,这将禁用重试。
请注意，这种重试行为与Feign默认行为不同，它会自动重试 IOExceptions,.将它们视为与网络相关的瞬态异常，以及从ErrorDecoder抛出的任何RetryableException

**修改重试机制：**

```java
@Configuration
public class FeignConfig {
   /**
    * OpenFeign配置重试机制bean
    * 该方法写入OpenFeign的配置类中
    * @return
    */
    @Bean
    public Retryer myRetryer() {
        //return Retryer.NEVER_RETRY; //Feign默认配置是不走重试策略的

        //最大请求次数为3(1初始次数+2重复次数)，初始间隔时间为100ms，重试间最大间隔时间为1s
        return new Retryer.Default(100,1,3);
    }
}
```

以上配置目前控制台没有看到3次重试过程，只看到结果，**正常的，正确的**，是feign的日志打印问题

### OpenFeign之性能优化HttpClient5

PpenFeign中http client 如果不做特殊配置，**OpenFeign默认使用**JDK自带的**HttpURLConnection**发送HTTP请求，由于默认HttpURLConnection没有连接池、**性能和效率比较低**，如果采用默认，性能上不是最牛B的。

**所以使用Apache HttpClient 5** 替换OpenFeign默认的HttpURLConnection 以**提升性能**。

**步骤**

1. pom
   ```html
   <!-- Apache HttpClient 5 替换OpenFeign默认的HttpURLConnection 以提升性能 -->
   <!-- httpclient5-->
   <dependency>
       <groupId>org.apache.httpcomponents.client5</groupId>
       <artifactId>httpclient5</artifactId>
       <version>5.3</version>
   </dependency>
   <!-- feign-hc5-->
   <dependency>
       <groupId>io.github.openfeign</groupId>
       <artifactId>feign-hc5</artifactId>
       <version>13.1</version>
   </dependency>
   ```

2. yml
   ```yaml
   #  Apache HttpClient5 配置开启
   spring:
     cloud:
       openfeign:
         httpclient:
           hc5:
             enabled: true
   ```

### OpenFeign请求回应压缩

**对请求和响应进行GZIP压缩**

Spring Cloud OpenFeign支持对请求和响应进行GZIP压缩，以**减少通信过程中的性能损耗**。

通过下面的两个参数设置，就能**开启请求与相应的压缩功能**：

```yaml
spring:
 cloud:
  openfeign:
   compression: # 压缩
    request: # 请求
     enabled: true # 开启请求压缩
    response: # 响应
     enabled: true # 开启响应压缩
```

**细粒度化设置**

对请求压缩做一些更细致的设置，比如下面的配置内容指定压缩的请求数据类型并设置了请求压缩的大小下限，

只有超过这个大小的请求才会进行压缩：

```yaml
spring:
 cloud:
  openfeign:
   compression:
    request:
     enabled: true # 开启请求压缩
     mime-types: text/xml,application/xml,application/json #触发压缩数据类型
     min-request-size: 2048 #最小触发压缩的大小
```

### OpenFeign日志打印功能

Feign 提供了日志打印功能，我们可以通过配置来调整日志级别，从而了解 Feign 中 Http 请求的细节，说白了就是**对Feign接口的调用情况进行监控和输出**

**日志级别：**

- `NONE`：默认的，不显示任何日志；
- `BASIC`：仅记录请求方法、URL、响应状态码及执行时间；
- `HEADERS`：除了 `BASIC` 中定义的信息之外，还有请求和响应的头信息；
- `FULL`：除了 `HEADERS` 中定义的信息之外，还有请求和响应的正文及元数据。

**步骤**

1. 配置日志bean
   ```java
   /**
     * OpenFeign配置日志bean
     * 该方法写入OpenFeign的配置类中
     * @return
     */
   @Bean
   Logger.Level feignLoggerLevel() {
       return Logger.Level.FULL;// 返回需要的日志级别
   }
   ```

2. yml里配置需要开启日志的feign客户端
   ```yaml
   # logging.level + 含有@FeignClient注解的完整带包名的接口名+debug级别
   # feign日志以什么级别监控哪个接口(OpenFeign对外暴露服务功能清单的接口)
   logging:
     level:
       com:
         xi:
           cloud:
             apis:
               PayFeignApi: debug 
   ```

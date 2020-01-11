package com.demo.test;

import com.demo.test.constant.Constant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Jack
 * @date 2019/04/15 start
 * @date 2019/10/09 continue
 * @EnableCaching: start cache base on annotations
 * <p>
 * ServletComponentScan: enable filter
 * 如果想用Swagger，要把@ServletComponentScan注释掉
 * <p>
 * 1. 要先启动数据库
 * 2. cd /Users/Jack_Cai/redis-4.0.11
 *    src/redis-server
 *    redis-cli
 *    redis-cli shutdown
 * 3. http://localhost:8080/swagger-ui.html
 *
 *  启动rabbitMQ
 *    sudo su     转换成超级用户
 *    cd /usr/local/etc/rabbitmq_server-3.5.3
 *    # 启动服务
 *    ./sbin/rabbitmq-server start
 *    # 停止服务
 *    ./sbin/rabbitmq-server stop
 *
 *    如果不能启动，报错：
 *    BOOT FAILED
 *    ===========Error description:
 *    find / -name  recovery.dets
 *    用 rm 命令删除掉 recoer.dets 文件
 *
 *    Rabbit Admin Page:
 *    http://localhost:15672/
 *    guest
 *    guest
 *
 *    cd /usr/local/etc/apache-jmeter-5.2.1/bin
 *    sh jmeter
 *
 * 4. 用swagger与用token有冲突,已解决
 * 5. 用MQ分离控制类和Service,已解决  12/19
 *    测试多线程到4000个，报错
 * java.net.SocketException: Connection reset
 * 	at java.net.SocketInputStream.read(SocketInputStream.java:210)
 * 	at java.net.SocketInputStream.read(SocketInputStream.java:141)
 * 	at org.apache.http.impl.io.SessionInputBufferImpl.streamRead(SessionInputBufferImpl.java:137)
 * 	at org.apache.http.impl.io.SessionInputBufferImpl.fillBuffer(SessionInputBufferImpl.java:153)
 * 	at org.apache.http.impl.io.SessionInputBufferImpl.readLine(SessionInputBufferImpl.java:280)
 * 	at org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:138)
 * 	at org.apache.http.impl.conn.DefaultHttpResponseParser.parseHead(DefaultHttpResponseParser.java:56)
 * 	at org.apache.http.impl.io.AbstractMessageParser.parse(AbstractMessageParser.java:259)
 * 	at org.apache.http.impl.DefaultBHttpClientConnection.receiveResponseHeader(DefaultBHttpClientConnection.java:163)
 * 	at org.apache.http.impl.conn.CPoolProxy.receiveResponseHeader(CPoolProxy.java:157)
 * 	at org.apache.http.protocol.HttpRequestExecutor.doReceiveResponse(HttpRequestExecutor.java:273)
 * 	at org.apache.http.protocol.HttpRequestExecutor.execute(HttpRequestExecutor.java:125)
 * 	at org.apache.http.impl.execchain.MainClientExec.execute(MainClientExec.java:272)
 * 	at org.apache.http.impl.execchain.ProtocolExec.execute(ProtocolExec.java:186)
 * 	at org.apache.http.impl.execchain.RetryExec.execute(RetryExec.java:89)
 * 	at org.apache.http.impl.execchain.RedirectExec.execute(RedirectExec.java:110)
 * 	at org.apache.http.impl.client.InternalHttpClient.doExecute(InternalHttpClient.java:185)
 * 	at org.apache.http.impl.client.CloseableHttpClient.execute(CloseableHttpClient.java:83)
 * 	at org.apache.jmeter.protocol.http.sampler.HTTPHC4Impl.executeRequest(HTTPHC4Impl.java:850)
 * 	at org.apache.jmeter.protocol.http.sampler.HTTPHC4Impl.sample(HTTPHC4Impl.java:561)
 * 	at org.apache.jmeter.protocol.http.sampler.HTTPSamplerProxy.sample(HTTPSamplerProxy.java:67)
 * 	at org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase.sample(HTTPSamplerBase.java:1282)
 * 	at org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase.sample(HTTPSamplerBase.java:1271)
 * 	at org.apache.jmeter.threads.JMeterThread.doSampling(JMeterThread.java:627)
 * 	at org.apache.jmeter.threads.JMeterThread.executeSamplePackage(JMeterThread.java:551)
 * 	at org.apache.jmeter.threads.JMeterThread.processSampler(JMeterThread.java:490)
 * 	at org.apache.jmeter.threads.JMeterThread.run(JMeterThread.java:257)
 * 	at java.lang.Thread.run(Thread.java:748)
 *
 * 6. 用Docker打包启动，没有完成
 * 7， Spring Cloud分布式架构，没完成
 * 8， Redis多级缓存，没完成
 * 9， 集成Nginx，没完成
 * 10，写一个高并发场景，没完成
 * 11，集成Kafka，没完成
 * 12, 集成MongoDB，没完成
 * 13，JVM调优，没完成
 * 14，安全性调优，没完成
 * 15，数据库MySQL主从集群，没完成
 * 16，秒杀场景，没完成
 * 17，限时订单功能，没完成
 *
 * @EnableCircuitBreaker 打开应用熔断服务降级
 */
@SpringBootApplication
//@EnableCircuitBreaker
@EnableCaching
// filter现在已经废弃
//@ServletComponentScan(basePackages = {"com.demo.test.filter"})
public class SpringBootJpaApplication {

    // 启动的时候要注意，由于我们在controller中注入了RestTemplate，所以启动的时候需要实例化该类的一个实例
    @Autowired
    private RestTemplateBuilder builder;
    BeanFactory beanFactory;

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

    /**
     * 这个用ConcurrentMapCache来实现缓存的
     */
    /*@Bean
    public CacheManagerCustomizer<ConcurrentMapCacheManager> cacheManagerCustomizer() {
        return new CacheManagerCustomizer<ConcurrentMapCacheManager>() {
            @Override
            public void customize(ConcurrentMapCacheManager cacheManager) {
                cacheManager.setAllowNullValues(false);
            }
        };
    }*/

    /**
     * 设置Redis缓存过期时间--最新版
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        return new RedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory),
                //  默认策略，未配置的 key 会使用这个
                this.getRedisCacheConfigurationWithTtl(Constant.SECONDS_30 * Constant.MINITES_IN_ONE_HOUR),
                // 指定 key 策略
                this.getRedisCacheConfigurationMap()
        );
    }

    /**
     *
     * @return
     */
    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        // 自定义设置缓存时间SsoCache和userCache进行过期时间配置
        redisCacheConfigurationMap.put("SsoCache", this.getRedisCacheConfigurationWithTtl(24 * 60 * 60));
        redisCacheConfigurationMap.put("userCache", this.getRedisCacheConfigurationWithTtl(30 * 60));
        return redisCacheConfigurationMap;
    }

    /**
     *
     * @param seconds
     * @return
     */
    private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Integer seconds) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.serializeValuesWith(
                RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(jackson2JsonRedisSerializer)
        ).entryTtl(Duration.ofSeconds(seconds));

        return redisCacheConfiguration;
    }

    /**
     *
     * @return
     */
    @Bean
    public KeyGenerator wiselyKeyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName());
                sb.append("." + method.getName());
                if (params == null || params.length == 0 || params[0] == null) {
                    return null;
                }
                String join = String.join("&", Arrays.stream(params).map(Object::toString).collect(Collectors.toList()));
                String format = String.format("%s{%s}", sb.toString(), join);
                //log.info("缓存key：" + format);
                return format;
            }
        };
    }

}

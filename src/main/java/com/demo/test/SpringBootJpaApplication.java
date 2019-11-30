package com.demo.test;

import com.demo.test.constant.Constant;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * src/redis-server
 * redis-cli
 * redis-cli shutdown
 * 3. http://localhost:8080/swagger-ui.html
 *    cd /usr/local/etc/apache-jmeter-5.2.1/bin
 *    sh jmeter
 *
 * 4. 用swagger与用token有冲突,已解决
 * 5. 用Docker打包启动，没有完成
 * 6. Spring Cloud分布式架构，没完成
 * 7， Redis多级缓存，没完成
 * 8， 写一个高并发场景，没完成
 * 9， 集成Mybatis，没完成
 * 10，集成Kafka，没完成
 * 11，集成Nginx，没完成
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

    // 使用RestTemplateBuilder来实例化RestTemplate对象，spring默认已经注入了RestTemplateBuilder实例
    @Bean
    public RestTemplate restTemplate() {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

    /* // 这个用ConcurrentMapCache来实现缓存的
    @Bean
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
     *
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

    private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        // 自定义设置缓存时间SsoCache和userCache进行过期时间配置
        redisCacheConfigurationMap.put("SsoCache", this.getRedisCacheConfigurationWithTtl(24 * 60 * 60));
        redisCacheConfigurationMap.put("userCache", this.getRedisCacheConfigurationWithTtl(30 * 60));
        return redisCacheConfigurationMap;
    }

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

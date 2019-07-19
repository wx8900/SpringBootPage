package com.demo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

/**
 * Started SpringBootJpaApplication in 8.796 seconds (JVM running for 9.378)
 * @EnableCaching: start cache base on annotations
 *
 * ServletComponentScan: enable filter
 * 如果想用Swagger，要把@ServletComponentScan注释掉
 *
 * @author Jack
 * @date 2019/04/15
 */
@SpringBootApplication
@EnableCaching
@ServletComponentScan(basePackages = {"com.demo.test.filter"})
public class SpringBootJpaApplication {
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
}

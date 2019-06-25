package com.demo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Started the Application in 8.814 seconds
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

}

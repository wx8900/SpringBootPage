package com.demo.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * Application
 *
 * @author Jack
 * @date 2019/04/15
 */
@SpringBootApplication
@ServletComponentScan(basePackages = {"com.demo.test.filter"})
public class SpringBootJpaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJpaApplication.class, args);
    }

}

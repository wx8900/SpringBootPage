package com.demo.test.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Exception : No handler found for GET /swagger-ui.html
 * <p>
 * o.s.web.servlet.PageNotFound : No handler found for GET /swagger-ui.html
 * 2019-10-07 18:26:33.659  WARN 12415 --- [nio-8080-exec-1] .w.s.m.s.DefaultHandlerExceptionResolver :
 * Resolved [org.springframework.web.servlet.NoHandlerFoundException: No handler found for GET /swagger-ui.html]
 * Add this class to fix the problem
 */
@Configuration
class WebMvcConfiguration extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}

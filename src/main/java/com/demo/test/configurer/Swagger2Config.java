package com.demo.test.configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2 Config class
 *
 * @author Jack
 * @date 2019/07/15
 */

@Configuration
@EnableSwagger2
public class Swagger2Config implements WebMvcConfigurer {
    public static final String BASE_PACKAGE = "com.demo.test";
    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())                // 生产环境的时候关闭 swagger 比较安全
                //.enable(enableSwagger)                //将Timestamp类型全部转为Long类型
                //.directModelSubstitute(Timestamp.class, Long.class) //将Date类型全部转为Long类型
                //.directModelSubstitute(Date.class, Long.class)
                .select()                // 扫描接口的包路径，不要忘记改成自己的
                .apis(RequestHandlerSelectors.basePackage(BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger RESTful API 文档")
                .description("Restful 风格接口")
                .termsOfServiceUrl("http://swagger.io/")
                //.contact(new Contact("Swagger", "127.0.0.1", "zhenghhgz@163.com"))
                .version("1.0")
                .build();
    }

}

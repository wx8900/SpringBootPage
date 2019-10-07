package com.demo.test.configurer;

import com.demo.test.constant.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author Jack
 * @date 2019/10/07  update
 *
 * @Profile 注解 标识加载在dev和test文件使用
 *
 */
@Configuration
@EnableSwagger2
@Profile({"dev", "test"})
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("spring-test-interface")
                //加载配置信息
                .apiInfo(apiInfo())
                // 生产环境的时候关闭 swagger 比较安全
                .enable(enableSwagger)
                //设置全局参数
                .globalOperationParameters(globalParamBuilder())
                //设置全局响应参数
                .globalResponseMessage(RequestMethod.GET,responseBuilder())
                .globalResponseMessage(RequestMethod.POST,responseBuilder())
                .globalResponseMessage(RequestMethod.PUT,responseBuilder())
                .globalResponseMessage(RequestMethod.DELETE,responseBuilder())
                .select()
                //加载swagger 扫描包
                .apis(RequestHandlerSelectors.basePackage(Constant.BASE_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                //设置安全认证
                .securitySchemes(securitySchemes());

    }

    /**
     * 获取swagger创建初始化信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger REST API")
                .description("©2019 Copyright. Powered By Jack.")
                // .termsOfServiceUrl("")
                .contact("xxxx@163.com")
                .license("Apache License Version 2.0")
                .licenseUrl("https://github.com/wx8900/SpringBootPage")
                .version("2.0")
                .build();
    }

    /**
     * 安全认证参数
     * @return
     */
    private List<ApiKey> securitySchemes() {
        List<ApiKey> list=new ArrayList<>();
        list.add(new ApiKey("token", "token", "header"));
        return list;
    }

    /**
     * 构建全局参数列表
     * @return
     */
    private List<Parameter> globalParamBuilder(){
        List<Parameter> pars = new ArrayList<>();
        pars.add(parameterBuilder("token","令牌","string","header",false).build());
        return pars;
    }

    /**
     * 创建参数
     * @return
     */
    private ParameterBuilder parameterBuilder(String name,String desc,String type ,String parameterType,boolean required) {
        ParameterBuilder tokenPar = new ParameterBuilder();
        tokenPar.name(name).description(desc).modelRef(new ModelRef(type)).parameterType(parameterType).required(required).build();
        return tokenPar;
    }

    /**
     * 创建全局响应值
     * @return
     */
    private List<ResponseMessage> responseBuilder() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        responseMessageList.add(new ResponseMessageBuilder().code(200).message("响应成功").build());
        responseMessageList.add(new ResponseMessageBuilder().code(500).message("服务器内部错误").build());
        return responseMessageList;
    }

}


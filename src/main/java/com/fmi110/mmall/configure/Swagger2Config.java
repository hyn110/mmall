package com.fmi110.mmall.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
//@EnableWebMvc
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                                                      .select()
                                                      .apis(RequestHandlerSelectors.basePackage("com.fmi110.mmall"))
                                                      .build();

    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("mmall商城 API")
                                   .description("小蜜蜂,嗡嗡嗡~~~")
                                   .licenseUrl("1009225458@qq.com")
                                   .termsOfServiceUrl("http://localhost:8080/mmall")
                                   .contact("fmi110")
                                   .version("1.0")
                                   .build();
    }
}
package com.example.dispatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfor())
                //分组名称
                .groupName("3.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.example.dispatch.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    public ApiInfo apiInfor(){
        return new ApiInfoBuilder()
                .title("swagger-bootstrap-ui-demo RESTful APIs")
                .description("# swagger-bootstrap-ui-demo RESTful APIs")
                .termsOfServiceUrl("http://www.xx.com/")
                .contact(new Contact("Nanimo","xxxx.com","xx@qq.com"))
                .version("3.0")
                .build();
    }

}
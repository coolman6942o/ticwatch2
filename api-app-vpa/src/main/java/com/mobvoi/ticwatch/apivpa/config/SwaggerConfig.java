package com.mobvoi.ticwatch.apivpa.config;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Project : ticwatch_backend
 * @Package Name : com.mobvoi.ticwatch.apivpa.config
 * @Author : xiekun
 * @Desc :
 * @Create Date : 2021年07月21日 16:17
 * ----------------- ----------------- -----------------
 */

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Value("${swagger.enable}")
  private boolean enableSwagger = true;

  @Value("${spring.profiles.active}")
  private String env;

  @Bean
  public Docket createAPI() {
    String path = env.equals("local") ? "/" : "/vpa/api/v1";
    Docket docket = new Docket(DocumentationType.SWAGGER_2)
        .enable(enableSwagger)
        .forCodeGeneration(true)
        .select()
//        .apis(RequestHandlerSelectors.basePackage("com.mobvoi.ticwatch"))
        .apis(RequestHandlerSelectors.any())
        //过滤生成链接
        .paths(PathSelectors.any())
        .build()
        .pathMapping(path)
        .apiInfo(apiInfo())
        .securitySchemes(Lists.newArrayList(apiKey()))
        .securityContexts(securityContexts());
    return docket;
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("TicWatch RESTful API文档")
        .version("1.0")
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("Authorization", "Authorization", "header");
  }

  private List<SecurityContext> securityContexts() {
    List<SecurityContext> securityContexts = new ArrayList<>();
    securityContexts.add(
        SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("^(?!auth).*$"))
            .build());
    return securityContexts;
  }

  List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    List<SecurityReference> securityReferences = new ArrayList<>();
    securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
    return securityReferences;
  }
}

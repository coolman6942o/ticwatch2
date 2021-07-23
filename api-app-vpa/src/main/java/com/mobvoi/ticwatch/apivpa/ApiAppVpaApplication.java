package com.mobvoi.ticwatch.apivpa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mobvoi"})
@MapperScan({"com.mobvoi.ticwatch.base.modules.mysql.account.mapper"
    ,"com.mobvoi.ticwatch.base.modules.mysql.ticwatch.mapper"})
@EnableSwagger2
public class ApiAppVpaApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiAppVpaApplication.class, args);
  }

}

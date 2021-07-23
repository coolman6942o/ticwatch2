package com.mobvoi.ticwatch.appwatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mobvoi.ticwatch.*"})
public class ServiceAppWatchApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceAppWatchApplication.class, args);
  }

}

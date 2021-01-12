package com.rockandpoll.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.rockandpoll.rest"})
public class RockAndPollApplication {

  public static void main(String[] args) {
    SpringApplication.run(RockAndPollApplication.class, args);
  }

}

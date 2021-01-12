package com.blockandpoll.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.blockandpoll.rest"})
public class BlockAndPollApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlockAndPollApplication.class, args);
  }

}

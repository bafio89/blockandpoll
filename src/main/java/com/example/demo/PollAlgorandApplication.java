package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication(scanBasePackages = {"com.pollalgorand.rest"})
public class PollAlgorandApplication {

	public static void main(String[] args) {
		SpringApplication.run(PollAlgorandApplication.class, args);
	}

}

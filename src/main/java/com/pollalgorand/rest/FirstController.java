package com.pollalgorand.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/bo")
public class FirstController {

  @GetMapping(value = "/helloworld")
  public String helloWorldEndPoint() {
    System.out.println("arrivata richiesta");
    return "This app is up and running";
  }
}

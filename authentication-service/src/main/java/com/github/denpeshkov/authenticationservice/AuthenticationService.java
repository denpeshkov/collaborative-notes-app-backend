package com.github.denpeshkov.authenticationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

/** Service used to authenticate (register) users */
@SpringBootApplication
public class AuthenticationService {

  /**
   * main method
   *
   * @param args params of commandline
   */
  public static void main(String[] args) {
    SpringApplication.run(AuthenticationService.class, args);
  }
}

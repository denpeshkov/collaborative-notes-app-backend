package com.github.denpeshkov.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Service that authenticates clients */
@SpringBootApplication
public class AuthenticationService {
  /**
   * Main method to start the service
   *
   * @param args standard CLI parameters
   */
  public static void main(String[] args) {
    SpringApplication.run(AuthenticationService.class, args);
  }
}

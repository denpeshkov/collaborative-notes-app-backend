package com.github.denpeshkov.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/** Service that proxies requests to other services, authentication, API composition etc. */
@SpringBootApplication
@EnableZuulProxy
public class APIGateway {
  /**
   * Main method used to start API gateway
   *
   * @param args CLI parameters
   */
  public static void main(String[] args) {
    SpringApplication.run(APIGateway.class, args);
  }
}

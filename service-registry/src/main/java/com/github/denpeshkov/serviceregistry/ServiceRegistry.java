package com.github.denpeshkov.serviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/** Service that manages instances of other services, locate them, health-check */
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistry {
  /**
   * Main method used to start service-registry
   *
   * @param args CLI parameters
   */
  public static void main(String[] args) {
    SpringApplication.run(ServiceRegistry.class, args);
  }
}

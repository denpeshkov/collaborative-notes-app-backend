package com.github.denpeshkov.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/** Service that manages configurations fot other services */
@SpringBootApplication
@EnableConfigServer
public class ConfigServer {
  /**
   * Main method used to start the service
   *
   * @param args standard cli parameters
   */
  public static void main(String[] args) {
    SpringApplication.run(ConfigServer.class, args);
  }
}

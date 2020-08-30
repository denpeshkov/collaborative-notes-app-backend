package com.github.denpeshkov.authenticationservice;

import com.github.denpeshkov.authenticationservice.jwt.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Service used to authenticate (register) users */
@SpringBootApplication
@RestController
public class AuthenticationService {

  /**
   * main method
   *
   * @param args params of commandline
   */
  public static void main(String[] args) {
    SpringApplication.run(AuthenticationService.class, args);
  }

  @GetMapping("/getData")
  String getData() {
    return "dome data";
  }

  @Bean
  public JwtConfig jwtConfig() {
    return new JwtConfig();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint() {
    return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
  }
}

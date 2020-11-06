package com.github.denpeshkov.authenticationservice.security;

import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;

@Configuration
public class SecurityConfig {
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Validated
  @ConfigurationProperties(prefix = "security.jwt")
  JwtConfig jwtConfig() {
    return new JwtConfig();
  }
}

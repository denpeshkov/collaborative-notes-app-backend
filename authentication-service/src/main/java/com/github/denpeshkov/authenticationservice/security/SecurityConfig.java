package com.github.denpeshkov.authenticationservice.security;

import com.github.denpeshkov.authenticationservice.exception.RestAuthenticationEntryPoint;
import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfig {
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationEntryPoint authenticationEntryPoint(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    return new RestAuthenticationEntryPoint(exceptionResolver);
  }

  @Bean
  JwtConfig jwtConfig() {
    return new JwtConfig();
  }
}

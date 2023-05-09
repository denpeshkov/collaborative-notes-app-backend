package com.github.denpeshkov.apigateway.security.security;

import com.github.denpeshkov.apigateway.security.exception.RestAuthenticationEntryPoint;
import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
public class SecurityConfig {
  @Bean
  AuthenticationEntryPoint authenticationEntryPoint(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
    return new RestAuthenticationEntryPoint(exceptionResolver);
  }

  @Bean
  @Validated
  @ConfigurationProperties(prefix = "security.jwt")
  JwtConfig jwtConfig() {
    return new JwtConfig();
  }
}

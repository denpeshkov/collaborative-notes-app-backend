package com.github.denpeshkov.authenticationservice.security;

import com.github.denpeshkov.authenticationservice.exception.RestAuthenticationEntryPoint;
import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

  /**
   * Configures {@link org.springframework.http.converter.json.Jackson2ObjectMapperBuilder} to
   * serialize {@link SimpleGrantedAuthority}
   *
   * @return Jackson customizer
   */
  @Bean
  @Order(1)
  Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
    return customizer ->
        customizer.mixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
  }
}

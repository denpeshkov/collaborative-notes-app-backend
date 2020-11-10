package com.github.denpeshkov.authenticationservice.security;

import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import javax.validation.Valid;

@SpringBootTest
@TestPropertySource("/jwt-security.properties")
class SecurityConfigTestIT {
  @Autowired JwtConfig jwtConfig;

  @Test
  void jwtConfig() {
    Assertions.assertNotNull(jwtConfig.getHeader());
    Assertions.assertNotNull(jwtConfig.getSchema());
    Assertions.assertNotNull(jwtConfig.getExpirationPeriod());
    Assertions.assertNotNull(jwtConfig.getSecret());
  }

  // Configuration used in test
  @Configuration
  @EnableAutoConfiguration
  static class JWTTest {
    @Bean
    @Valid
    @ConfigurationProperties(prefix = "security.jwt")
    JwtConfig jwtConfig() {
      return new JwtConfig();
    }
  }
}

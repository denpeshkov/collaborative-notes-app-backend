package com.github.denpeshkov.apigateway.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.Period;

/** Configurations for JWT tokens */
@ConfigurationProperties(prefix = "security.jwt")
@ConstructorBinding
@Validated
public class JwtConfig {
  /** Authorization header */
  @NotBlank private String header;

  /** Bearer schema */
  @NotBlank private String schema;

  /** Duration since token creation date after which token is expired */
  private Period expirationPeriod;

  /** Token secret */
  @NotBlank private String secret;

  public String getHeader() {
    return header;
  }

  public String getSchema() {
    return schema;
  }

  public Period getExpirationPeriod() {
    return expirationPeriod;
  }

  public String getSecret() {
    return secret;
  }
}

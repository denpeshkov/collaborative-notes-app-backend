package com.github.denpeshkov.authenticationservice.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Period;

/** Configurations for JWT tokens */
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
@Validated
public class JwtConfig {

  /** Authorization header */
  @NotBlank private String header;

  /** Bearer schema */
  @NotBlank private String schema;

  /** Duration since token creation date after which token is expired */
  @NotNull private Period expirationPeriod;

  /** Token secret */
  @NotBlank private String secret;

  public void setHeader(String header) {
    this.header = header;
  }

  public void setSchema(String schema) {
    this.schema = schema;
  }

  public void setExpirationPeriod(Period expirationPeriod) {
    this.expirationPeriod = expirationPeriod;
  }

  public void setSecret(String secret) {
    this.secret = secret;
  }

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

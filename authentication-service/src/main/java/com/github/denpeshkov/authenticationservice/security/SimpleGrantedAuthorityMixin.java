package com.github.denpeshkov.authenticationservice.security;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Jackson Mix-In class used to conigure serialization of {@link
 * org.springframework.security.core.authority.SimpleGrantedAuthority}
 */
public abstract class SimpleGrantedAuthorityMixin {

  @JsonValue
  public abstract String toString();
}

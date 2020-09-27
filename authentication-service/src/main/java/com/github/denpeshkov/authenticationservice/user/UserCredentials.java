package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

@JsonIgnoreProperties(
    ignoreUnknown = true,
    value = {"authorities"},
    allowGetters = true)
public class UserCredentials {
  private final String username;
  private final String password;
  @JsonRawValue private final Set<GrantedAuthority> authorities;

  @JsonCreator
  public UserCredentials(
      @JsonProperty("username") String username, @JsonProperty("password") String password) {
    this.username = username;
    this.password = password;
    authorities = Collections.emptySet();
  }

  public UserCredentials(String username, String password, Set<GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Set<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserCredentials that = (UserCredentials) o;
    return username.equals(that.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(username);
  }

  @Override
  public String toString() {
    return "UserCredentials{"
        + "username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", authorities="
        + authorities
        + '}';
  }
}

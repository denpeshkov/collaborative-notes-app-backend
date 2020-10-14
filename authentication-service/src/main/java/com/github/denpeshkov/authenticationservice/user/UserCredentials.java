package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/** Represent user's credentials during authentication and registration */
@JsonIgnoreProperties(ignoreUnknown = true, allowGetters = true)
@Table("USER_CREDENTIALS")
public class UserCredentials {
  /** Id used to persists entity in DB */
  private @Id Long id;
  /** user's username */
  private String username;
  /** user's password */
  private String password;
  /**
   * user's roles
   *
   * <p><b>not used at the moment !!! so {@link Transient}
   */
  @JsonRawValue @Transient private Set<SimpleGrantedAuthority> authorities;

  /**
   * Creates entity from {@link UserDetails} instance to work with Spring Security abstractions
   *
   * @param userDetails {@link UserDetails} instance
   */
  public UserCredentials(UserDetails userDetails) {
    username = userDetails.getUsername();
    password = userDetails.getPassword();
    authorities = (Set<SimpleGrantedAuthority>) userDetails.getAuthorities();
  }

  @JsonCreator
  @PersistenceConstructor
  public UserCredentials(
      @JsonProperty("username") String username, @JsonProperty("password") String password) {
    this.username = username;
    this.password = password;
    authorities = Collections.emptySet();
  }

  // constructor used to persist entity
  public UserCredentials(
      String username, String password, Set<SimpleGrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setAuthorities(Set<SimpleGrantedAuthority> authorities) {
    this.authorities = authorities;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public Set<SimpleGrantedAuthority> getAuthorities() {
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

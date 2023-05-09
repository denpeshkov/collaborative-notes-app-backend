package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/** Represent user's credentials during authentication and registration */
@JsonIgnoreProperties(ignoreUnknown = true, allowGetters = true)
@Table("USER_CREDENTIALS")
public class UserCredentials {
  /** Id used to persists entity in DB */
  private @Id Long id;
  /** user's username */
  @NotBlank(message = "username should not be empty")
  private String username;
  /** user's password */
  @NotBlank(message = "password should not be empty")
  private String password;

  // JPA spec
  public UserCredentials() {}

  @JsonCreator
  @PersistenceConstructor // constructor used to persist entity
  public UserCredentials(
      @JsonProperty("username") String username, @JsonProperty("password") String password) {
    this.username = username;
    this.password = password;
  }

  public UserCredentials(UserCredentials userCredentials) {
    username = userCredentials.getUsername();
    password = userCredentials.getPassword();
  }

  // fluent API
  public UserCredentials setUsername(String username) {
    this.username = username;
    return this;
  }

  // fluent API
  public UserCredentials setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public void updateUser(UserCredentials userCredentials) {
    this.username = userCredentials.username;
    this.password = userCredentials.password;
  }

  /**
   * Users are equal if their usernames are equals
   *
   * @param o user
   * @return {@code true} if users are equal, otherwise {@code false}
   */
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
        + '}';
  }
}

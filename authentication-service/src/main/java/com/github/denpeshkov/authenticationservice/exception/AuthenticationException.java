package com.github.denpeshkov.authenticationservice.exception;

/** Base class for authentication exceptions */
public class AuthenticationException extends Exception {
  public AuthenticationException() {
    super();
  }

  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }

  public AuthenticationException(Throwable cause) {
    super(cause);
  }
}

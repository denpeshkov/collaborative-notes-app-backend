package com.github.denpeshkov.apigateway.security.exception;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@link AuthenticationEntryPoint} used to delegate exception handling to {@link
 * RestExceptionHandler} class
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final HandlerExceptionResolver exceptionResolver;

  public RestAuthenticationEntryPoint(HandlerExceptionResolver exceptionResolver) {
    this.exceptionResolver = exceptionResolver;
  }

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    exceptionResolver.resolveException(request, response, null, authException);
  }
}

package com.github.denpeshkov.apigateway.security.security;

import com.github.denpeshkov.apigateway.security.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
  private final JwtConfig jwtConfig;
  final AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  public WebSecurityConfigurer(
      JwtConfig jwtConfig, AuthenticationEntryPoint authenticationEntryPoint) {
    this.jwtConfig = jwtConfig;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        // make sure we use stateless session; session won't be used to store user's state.
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // handle an authorized attempts
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        // Add a filter to validate the tokens with every request
        .addFilterAfter(
            new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
        // authorization requests config
        .authorizeRequests()
        // allow all who are accessing authentication-service
        .antMatchers(HttpMethod.POST, "/authentication-service")
        .permitAll()
        // Any other request must be authenticated
        .anyRequest()
        .authenticated();
  }
}

package com.github.denpeshkov.authenticationservice.security;

import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

/** Spring Security configuration */
@EnableWebSecurity // Enable security config. This annotation denotes config for spring security.
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final JwtConfig jwtConfig;
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public WebSecurityConfigurer(
      @Qualifier("userService") UserDetailsService userDetailsService,
      JwtConfig jwtConfig,
      AuthenticationEntryPoint authenticationEntryPoint,
      BCryptPasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.jwtConfig = jwtConfig;
    this.authenticationEntryPoint = authenticationEntryPoint;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        .addFilter(new JwtLoginFilter(authenticationManager(), jwtConfig))
        .authorizeRequests()
        // предоставить доступ к login и signup и actuator endpoints
        .antMatchers("/login/**", "/signup", "/actuator/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }

  /*  @Override
  public void configure(WebSecurity web) {
    // предоставить доступ к actuator endpoints
    web.ignoring().requestMatchers(EndpointRequest.toAnyEndpoint());
  }*/

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}

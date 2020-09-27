package com.github.denpeshkov.authenticationservice.security;

import com.github.denpeshkov.authenticationservice.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.util.Collections;

/** Spring Security configuration */
@EnableWebSecurity // Enable security config. This annotation denotes config for spring security.
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

  private final UserDetailsService userDetailsService;
  private final JwtConfig jwtConfig;
  private final AuthenticationEntryPoint authenticationEntryPoint;
  private final BCryptPasswordEncoder passwordEncoder;

  @Autowired
  public WebSecurityConfigurer(
      @Qualifier("inMemoryUserDetailsManager") UserDetailsService userDetailsService,
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
        .antMatchers(HttpMethod.POST, "/login/**")
        .permitAll()
        .anyRequest()
        .authenticated();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    ((UserDetailsManager) userDetailsService)
        .createUser(new User("aaa", passwordEncoder.encode("aaa"), Collections.emptyList()));

    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}

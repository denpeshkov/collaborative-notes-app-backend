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
        // make sure we use stateless session; session won't be used to store user's state.
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        // handle an authorized attempts
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint)
        .and()
        // Add a filter to validate user credentials and add token in the response header

        // What's the authenticationManager()?
        // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing
        // user's credentials
        // The filter needs this auth manager to authenticate the user.
        .addFilter(new JwtLoginFilter(authenticationManager(), jwtConfig))
        .authorizeRequests()
        // allow all POST requests
        .antMatchers(HttpMethod.POST, "/login/**")
        .permitAll()
        // any other requests must be authenticated
        .anyRequest()
        .authenticated();
  }

  // Spring has UserDetailsService interface, which can be overriden to provide our implementation
  // for fetching user from database (or any other source).
  // The UserDetailsService object is used by the auth manager to load the user from database.
  // In addition, we need to define the password encoder also. So, auth manager can compare and
  // verify passwords.
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    ((UserDetailsManager) userDetailsService)
        .createUser(new User("aaa", passwordEncoder.encode("aaa"), Collections.emptyList()));

    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
  }
}

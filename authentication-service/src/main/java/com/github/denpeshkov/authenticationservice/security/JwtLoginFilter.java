package com.github.denpeshkov.authenticationservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denpeshkov.authenticationservice.jwt.JwtConfig;
import com.github.denpeshkov.authenticationservice.user.UserCredentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.stream.Collectors;

/** Filter used to authenticate (login) user and sent JWT token after successful authentication */
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

  private static final ObjectMapper mapper = new ObjectMapper();
  private final AuthenticationManager authManager;
  private final JwtConfig jwtConfig;

  public JwtLoginFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
    this.authManager = authManager;
    this.jwtConfig = jwtConfig;

    // By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
    // In our case, we use "/login". So, we need to override the defaults.
    this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login/**", "POST"));
  }

  /**
   * Authenticates user
   *
   * @param request request
   * @param response response
   * @return the authenticated user token (not JWT), or null if authentication is incomplete
   * @throws AuthenticationException if authentication fails
   */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    try {
      // 1. Get credentials from request
      UserCredentials creds = mapper.readValue(request.getInputStream(), UserCredentials.class);

      // 2. Create auth object (contains credentials) which will be used by auth manager
      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(
              creds.getUsername(), creds.getPassword(), Collections.emptyList());

      // 3. Authentication manager authenticate the user, and use
      // UserDetialsServiceImpl::loadUserByUsername() method to load the user.
      return authManager.authenticate(authToken);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Upon successful authentication, generate a JWT token
   *
   * @param request request
   * @param response response
   * @param chain filter chain
   * @param auth the current authenticated user
   */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication auth) {

    Instant now = Instant.now();
    SecretKey secretKey =
        Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));

    String token =
        Jwts.builder()
            .setSubject(auth.getName())
            .setIssuer("collaborative-notes-app")
            .setIssuedAt(Date.from(now))
            // Convert to list of strings.
            // This is important because it affects the way we get them back in the Gateway.
            .claim(
                "authorities",
                auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList()))
            .setExpiration(Date.from(now.plus(jwtConfig.getExpirationPeriod())))
            .signWith(secretKey)
            .compact();

    // Add token to header
    response.addHeader(jwtConfig.getHeader(), jwtConfig.getSchema() + " " + token);
  }
}

package com.github.denpeshkov.apigateway.security.security;

import com.github.denpeshkov.apigateway.security.jwt.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;

/**
 * Filter used to authenticate users who have JWT token due to previous login request to application
 */
public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtConfig jwtConfig;

  public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    // 1. get the authentication header. Tokens are supposed to be passed in the authentication
    // header
    String header = request.getHeader(jwtConfig.getHeader());

    // 2. validate the header and check the prefix
    if (header == null || !header.startsWith(jwtConfig.getSchema())) {
      chain.doFilter(request, response); // If not valid, go to the next filter.
      return;
    }

    // If there is no token provided and hence the user won't be authenticated.
    // It's Ok. Maybe the user accessing a public path or asking for a token.

    // All secured paths that needs a token are already defined and secured in config class.
    // And If user tried to access without access token, then he won't be authenticated and an
    // exception will be thrown.

    // 3. Get the token
    String token = header.replace(jwtConfig.getSchema(), "");

    try { // exceptions might be thrown in creating the claims if for example the token is expired
      // 4. Validate the token

      SecretKey secretKey =
          Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));

      Claims claims =
          Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

      if (validate(claims)) {

        // 5. Create auth object
        // UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the
        // current authenticated / being authenticated user.
        // It needs a list of authorities, which has type of GrantedAuthority interface, where
        // SimpleGrantedAuthority is an implementation of that interface
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, Collections.emptySet());

        // 6. Authenticate the user
        // Now, user is authenticated
        SecurityContextHolder.getContext().setAuthentication(auth);
      }

    } catch (Exception e) {
      // In case of failure. Make sure it's clear; so guarantee user won't be authenticated
      SecurityContextHolder.clearContext();
    }

    // go to the next filter in the filter chain
    chain.doFilter(request, response);
  }

  /**
   * Validates JWT token
   *
   * @param claims claims of JWT token
   * @return true if valid, false if invalid
   */
  private boolean validate(Claims claims) {
    String issuer = claims.getIssuer();
    String username = claims.getSubject();
    Date issuedAt = claims.getIssuedAt();
    Date expiration = claims.getExpiration();

    return (issuer != null && issuer.equals("collaborative-notes-app"))
        && username != null
        && (issuedAt != null && issuedAt.before(Date.from(Instant.now())))
        && (expiration != null && expiration.after(Date.from(Instant.now())));
  }
}

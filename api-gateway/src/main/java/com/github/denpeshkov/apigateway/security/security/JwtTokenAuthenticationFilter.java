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
    String header = request.getHeader(jwtConfig.getHeader());

    if (header == null || !header.startsWith(jwtConfig.getSchema())) {
      chain.doFilter(request, response); // If not valid, go to the next filter.
      return;
    }

    String token = header.replace(jwtConfig.getSchema(), "");

    try {
      SecretKey secretKey =
          Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));

      Claims claims =
          Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

      if (validate(claims)) {
        UsernamePasswordAuthenticationToken auth =
            new UsernamePasswordAuthenticationToken(
                claims.getSubject(), null, Collections.emptySet());

        SecurityContextHolder.getContext().setAuthentication(auth);
      }

    } catch (Exception e) {
      SecurityContextHolder.clearContext();
    }

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

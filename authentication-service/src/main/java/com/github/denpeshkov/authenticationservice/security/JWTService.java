package com.github.denpeshkov.authenticationservice.security;

import com.github.denpeshkov.authenticationservice.user.UserCredentials;
import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;

/** Service to create and verify JWT tokens */
@Service
public class JWTService {
  private final JwtConfig jwtConfig;

  @Autowired
  public JWTService(JwtConfig jwtConfig) {
    this.jwtConfig = jwtConfig;
  }

  public String createToken(UserCredentials userCredentials) {
    Instant now = Instant.now();

    SecretKey secretKey =
        Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes(StandardCharsets.UTF_8));

    return Jwts.builder()
        .setSubject(userCredentials.getUsername())
        .setIssuer("collaborative-notes-app")
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(jwtConfig.getExpirationPeriod())))
        .signWith(secretKey)
        .compact();
  }
}

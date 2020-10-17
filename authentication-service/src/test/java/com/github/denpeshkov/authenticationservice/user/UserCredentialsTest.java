package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denpeshkov.authenticationservice.security.SimpleGrantedAuthorityMixin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserCredentialsTest {
  private JacksonTester<UserCredentials> jacksonTester;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeAll
  static void beforeAll() throws URISyntaxException {
    objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
  }

  @BeforeEach
  void beforeEach() {
    JacksonTester.initFields(this, objectMapper);
  }

  @Test
  void testSerializationWithAuthorities() throws IOException {
    // to preserve insertion order
    Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
    authorities.add(new SimpleGrantedAuthority("ADMIN"));
    authorities.add(new SimpleGrantedAuthority("USER"));

    UserCredentials userCredentialsWithAuthorities =
        new UserCredentials("root", "root", authorities);

    String userCredentialsJson = jacksonTester.write(userCredentialsWithAuthorities).getJson();

    UserCredentials userCredentials = jacksonTester.parse(userCredentialsJson).getObject();

    assertEquals(userCredentialsWithAuthorities, userCredentials);
  }

  @Test
  void testSerializationWithoutAuthorities() throws IOException {
    UserCredentials userCredentialsWithoutAuthorities = new UserCredentials("root", "root");

    String userCredentialsJson = jacksonTester.write(userCredentialsWithoutAuthorities).getJson();

    UserCredentials userCredentials = jacksonTester.parse(userCredentialsJson).getObject();

    assertEquals(userCredentialsWithoutAuthorities, userCredentials);
  }
}

package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserCredentialsTest {
  private JacksonTester<UserCredentials> jacksonTester;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static UserCredentials userCredentialsWithAuthorities;
  private static UserCredentials userCredentialsWithoutAuthorities;
  private static URI userCredentialsWithAuthoritiesJson;
  private static URI userCredentialsWithoutAuthoritiesJson;

  @BeforeAll
  static void beforeAll() throws URISyntaxException {
    // to preserve insertion order
    Set<SimpleGrantedAuthority> authorities = new LinkedHashSet<>();
    authorities.add(new SimpleGrantedAuthority("ADMIN"));
    authorities.add(new SimpleGrantedAuthority("USER"));

    userCredentialsWithAuthorities = new UserCredentials("root", "root", authorities);

    userCredentialsWithoutAuthorities = new UserCredentials("root", "root");

    userCredentialsWithAuthoritiesJson =
        UserCredentialsTest.class
            .getClassLoader()
            .getResource("UserCredentialsWithAuthorities.json")
            .toURI();

    userCredentialsWithoutAuthoritiesJson =
        UserCredentialsTest.class
            .getClassLoader()
            .getResource("UserCredentialsWithoutAuthorities.json")
            .toURI();
  }

  @BeforeEach
  void beforeEach() {
    JacksonTester.initFields(this, objectMapper);
  }

  @Test
  void testDesiralizationWithoutAuthorities() throws IOException {
    String json = Files.readString(Paths.get(userCredentialsWithoutAuthoritiesJson));

    assertThat(jacksonTester.parse(json))
        .isEqualToComparingFieldByField(userCredentialsWithoutAuthorities);
  }

  @Test
  void testDesiralizationWithAuthorities() throws IOException {
    String json = Files.readString(Paths.get(userCredentialsWithAuthoritiesJson));

    assertThat(jacksonTester.parse(json))
        .isEqualToComparingFieldByField(userCredentialsWithAuthorities);
  }

  @Test
  void testSerializationWithoutAuthorities() throws IOException {
    assertThat(jacksonTester.write(userCredentialsWithoutAuthorities))
        .isStrictlyEqualToJson(new File(userCredentialsWithoutAuthoritiesJson));
  }

  @Test
  void testSerializationWithAuthorities() throws IOException {
    assertThat(jacksonTester.write(userCredentialsWithAuthorities))
        .isStrictlyEqualToJson(new File(userCredentialsWithAuthoritiesJson));
  }
}

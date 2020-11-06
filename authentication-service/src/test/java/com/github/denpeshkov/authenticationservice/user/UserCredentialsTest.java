package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserCredentialsTest {
  private JacksonTester<UserCredentials> jacksonTester;
  private static final ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void beforeEach() {
    JacksonTester.initFields(this, objectMapper);
  }

  @Test
  void testSerializationWithoutAuthorities() throws IOException {
    UserCredentials userCredentialsWithoutAuthorities = new UserCredentials("root", "root");

    String userCredentialsJson = jacksonTester.write(userCredentialsWithoutAuthorities).getJson();

    UserCredentials userCredentials = jacksonTester.parse(userCredentialsJson).getObject();

    assertEquals(userCredentialsWithoutAuthorities, userCredentials);
  }
}

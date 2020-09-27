package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

class UserCredentialsTest {
  private static final ObjectMapper mapper = new ObjectMapper();
  private static String request;

  @BeforeEach
  void setUp() {
    request = "{\"username\":\"aaa\", \"password\":\"aaa\"}";
  }

  @Test
  void testDesiralization() throws JsonProcessingException {
    UserCredentials userCredentials = mapper.readValue(request, UserCredentials.class);

    System.out.println(userCredentials);
  }

  @Test
  void testSerialization() throws JsonProcessingException {
    UserCredentials userCredentials =
        new UserCredentials(
            "aa",
            "aaaaa",
            Set.of(new SimpleGrantedAuthority("ROLE_1"), new SimpleGrantedAuthority("ROLE_2")));

    System.out.println(userCredentials);

    System.out.println(mapper.writeValueAsString(userCredentials));
  }
}

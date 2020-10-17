package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denpeshkov.authenticationservice.security.SecurityConfig;
import com.github.denpeshkov.authenticationservice.security.SimpleGrantedAuthorityMixin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@TestPropertySource("/bootstrap.yml")
@ExtendWith(MockitoExtension.class)
@Import(SecurityConfig.class)
class UserControllerTestIT {
  @MockBean(name = "userService")
  private UserService userService;

  @Autowired private MockMvc mockMvc;
  @Captor ArgumentCaptor<User> userCaptor;

  // used to emulate browser send JSON requests
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private ResultMatcher httpRequestMethodNotSupportedExceptionResultMatcher() {
    return result ->
        assertTrue(result.getResolvedException() instanceof HttpRequestMethodNotSupportedException);
  }

  private ResultMatcher httpMessageNotReadableExceptionResultMatcher() {
    return result ->
        assertTrue(result.getResolvedException() instanceof HttpMessageNotReadableException);
  }

  @BeforeAll
  static void beforeAll() {
    // adds Mix-In for given object mapper to use in "browser"
    // Spring registers our SimpleGrantedAuthorityMixin to use for serialization and deserialization
    objectMapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
  }

  @Test
  void registerUser_WhenNonPostRequest() throws Exception {
    mockMvc
        .perform(get("/signup"))
        .andExpect(httpRequestMethodNotSupportedExceptionResultMatcher())
        .andExpect(status().isMethodNotAllowed())
        .andDo(print());

    mockMvc
        .perform(put("/signup"))
        .andExpect(httpRequestMethodNotSupportedExceptionResultMatcher())
        .andExpect(status().isMethodNotAllowed())
        .andDo(print());

    mockMvc
        .perform(delete("/signup"))
        .andExpect(httpRequestMethodNotSupportedExceptionResultMatcher())
        .andExpect(status().isMethodNotAllowed())
        .andDo(print());

    // options is allowed by default
    mockMvc.perform(options("/signup")).andExpect(status().isOk()).andDo(print());

    mockMvc
        .perform(head("/signup"))
        .andExpect(httpRequestMethodNotSupportedExceptionResultMatcher())
        .andExpect(status().isMethodNotAllowed())
        .andDo(print());

    mockMvc
        .perform(patch("/signup"))
        .andExpect(httpRequestMethodNotSupportedExceptionResultMatcher())
        .andExpect(status().isMethodNotAllowed())
        .andDo(print());
  }

  @Test
  void registerUser_WhenPostRequestWithIncorrectBody() throws Exception {
    mockMvc
        .perform(post("/signup"))
        .andExpect(status().isBadRequest())
        .andExpect(httpMessageNotReadableExceptionResultMatcher())
        .andDo(print());

    mockMvc
        .perform(
            post("/signup")
                .content("{\"someField\":\"someValue\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  void registerUser_WhenPostRequestWithAuthorities() throws Exception {
    UserCredentials userCredentials =
        new UserCredentials(
            "root",
            "root",
            Set.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER")));

    User user =
        new User(
            userCredentials.getUsername(),
            userCredentials.getPassword(),
            userCredentials.getAuthorities());

    mockMvc
        .perform(
            post("/signup")
                .content(objectMapper.writeValueAsString(userCredentials))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andDo(print());

    verify(userService, times(1)).createUser(userCaptor.capture());
    assertEquals(userCaptor.getValue(), user);
  }

  @Test
  void registerUser_WhenPostRequestWithoutAuthorities() throws Exception {
    UserCredentials userCredentials = new UserCredentials("root", "root");
    User user =
        new User(
            userCredentials.getUsername(),
            userCredentials.getPassword(),
            userCredentials.getAuthorities());

    mockMvc
        .perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCredentials)))
        .andExpect(status().isCreated())
        .andDo(print());

    verify(userService, times(1)).createUser(userCaptor.capture());
    assertEquals(userCaptor.getValue(), user);
  }

  @Test
  void testActuatorEndpoints() throws Exception {
    mockMvc.perform(get("/actuator")).andDo(print());
  }
}

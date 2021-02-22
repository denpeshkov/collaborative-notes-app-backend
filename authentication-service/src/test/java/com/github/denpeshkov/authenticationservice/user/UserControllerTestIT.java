package com.github.denpeshkov.authenticationservice.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.denpeshkov.authenticationservice.exception.RestExceptionHandler;
import com.github.denpeshkov.authenticationservice.exception.UserAlreadyExistsException;
import com.github.denpeshkov.authenticationservice.exception.UserNotFoundException;
import com.github.denpeshkov.authenticationservice.security.JWTService;
import com.github.denpeshkov.authenticationservice.security.SecurityConfig;
import com.github.denpeshkov.commons.security.jwt.JwtConfig;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@Import({SecurityConfig.class, RestExceptionHandler.class, JWTService.class})
class UserControllerTestIT {
  @MockBean(name = "userService")
  private UserService userService;

  @MockBean private JwtConfig jwtConfig;
  @MockBean private JWTService jwtService;

  @Autowired private MockMvc mockMvc;
  @Captor ArgumentCaptor<UserCredentials> userCaptor;

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

  @Test
  void registerUser() throws Exception {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    mockMvc
        .perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCredentials)))
        .andExpect(status().isCreated())
        .andDo(print());

    verify(userService, times(1)).createUser(userCaptor.capture());
    assertEquals(userCaptor.getValue(), userCredentials);
  }

  @Test
  void registerUserWhenAlreadyExists() throws Exception {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    doThrow(
            new UserAlreadyExistsException(
                "Cannot create a user with given username: root! User with given username already exists!"))
        .when(userService)
        .createUser(any());

    mockMvc
        .perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCredentials)))
        .andExpect(status().isUnauthorized())
        .andDo(print());
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
  void registerUser_WhenUserAlreadyLogged() throws Exception {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    mockMvc
        .perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCredentials))
                .header("Authorization", "jwt_token"))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void loginUser() throws Exception {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    when(jwtService.createToken(any())).thenReturn("some_valid_token");

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCredentials)))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("{'jwtToken':'some_valid_token'}"))
        .andDo(print());
  }

  @Test
  void loginUser_WhenUserAlreadyLogged() throws Exception {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCredentials))
                .header("Authorization", "jwt_token"))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void loginUser_WhenUserDoesntExist() throws Exception {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    doThrow(new UserNotFoundException("User with given username: root is not found!"))
        .when(userService)
        .verifyUser(any());

    mockMvc
        .perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCredentials)))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  void loginUser_WhenPostRequestWithIncorrectBody() throws Exception {
    mockMvc
        .perform(post("/login"))
        .andExpect(status().isBadRequest())
        .andExpect(httpMessageNotReadableExceptionResultMatcher())
        .andDo(print());

    mockMvc
        .perform(
            post("/login")
                .content("{\"someField\":\"someValue\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print());
  }

  @Test
  void testActuatorEndpoints() throws Exception {
    mockMvc.perform(get("/actuator")).andDo(print());
  }
}

package com.github.denpeshkov.apigateway.security.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(AuthenticationService_UserControllerMock.class)
class RestAuthenticationEntryPointTestIT {

  @Autowired private MockMvc mockMvc;

  @Test
  void commence() throws Exception {
    mockMvc
        .perform(get("/private-resource"))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.status", is("UNAUTHORIZED")))
        .andExpect(jsonPath("$.timestamp").exists())
        .andExpect(jsonPath("$.message").exists())
        .andExpect(jsonPath("$.debugMessage").exists())
        .andDo(print());

    mockMvc
        .perform(get("/authentication-service/signup"))
        .andExpect(status().isCreated())
        .andDo(print());
    mockMvc
        .perform(get("/authentication-service/login"))
        .andExpect(status().isOk())
        .andExpect(
            result ->
                Assertions.assertEquals("jwt_token", result.getResponse().getContentAsString()))
        .andDo(print());

    mockMvc
        .perform(get("/authentication-service/signup").header("Authorization", "token"))
        .andExpect(status().isNotFound())
        .andDo(print());
    mockMvc
        .perform(get("/authentication-service/login").header("Authorization", "token"))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  void testEndpoints() throws Exception {
    mockMvc.perform(get("/actuator")).andExpect(status().isOk()).andDo(print());
  }
}

/** Test class to mock authentication-service UserController controller */
@RestController
@RequestMapping("/authentication-service")
class AuthenticationService_UserControllerMock {

  @RequestMapping(path = "/signup", headers = "!Authorization")
  @ResponseStatus(HttpStatus.CREATED)
  void registerUser() {
    //
  }

  @GetMapping(path = "/login", headers = "!Authorization")
  @ResponseStatus(HttpStatus.OK)
  String loginUser() {
    return "jwt_token";
  }
}

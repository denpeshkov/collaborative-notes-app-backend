package com.github.denpeshkov.authenticationservice.user;

import com.github.denpeshkov.authenticationservice.exception.IncorrectPasswordException;
import com.github.denpeshkov.authenticationservice.exception.UserAlreadyExistsException;
import com.github.denpeshkov.authenticationservice.exception.UserNotFoundException;
import com.github.denpeshkov.authenticationservice.security.JWTService;
import com.github.denpeshkov.commons.security.jwt.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class UserController {
  private final UserService userService;
  private final JWTService jwtService;
  private final JwtConfig jwtConfig;

  @Autowired
  public UserController(UserService userService, JWTService jwtService, JwtConfig jwtConfig) {
    this.userService = userService;
    this.jwtService = jwtService;
    this.jwtConfig = jwtConfig;
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  void registerUser(@RequestBody @Valid UserCredentials userCredentials)
      throws UserAlreadyExistsException {
    userService.createUser(userCredentials);
  }

  @PostMapping("/login")
  void loginUser(@RequestBody @Valid UserCredentials userCredentials, HttpServletResponse response)
      throws UserNotFoundException, IncorrectPasswordException {
    try {
      userService.verifyUser(userCredentials);
    } catch (UserNotFoundException e) {
      throw new UserNotFoundException(
          String.format(
              "User with given username: %s is not registered! Register the user first!",
              userCredentials.getUsername()),
          e);
    }

    String token = jwtService.createToken(userCredentials);

    response.setHeader(jwtConfig.getHeader(), jwtConfig.getSchema() + " " + token);
  }
}

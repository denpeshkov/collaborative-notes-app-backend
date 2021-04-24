package com.github.denpeshkov.authenticationservice.user;

import com.github.denpeshkov.authenticationservice.exception.IncorrectPasswordException;
import com.github.denpeshkov.authenticationservice.exception.UserAlreadyExistsException;
import com.github.denpeshkov.authenticationservice.exception.UserNotFoundException;
import com.github.denpeshkov.authenticationservice.security.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
  private final UserService userService;
  private final JWTService jwtService;

  @Autowired
  public UserController(UserService userService, JWTService jwtService) {
    this.userService = userService;
    this.jwtService = jwtService;
  }

  @PostMapping(path = "/signup", headers = "!Authorization")
  @ResponseStatus(HttpStatus.CREATED)
  void registerUser(@RequestBody @Valid UserCredentials userCredentials)
      throws UserAlreadyExistsException {
    userService.createUser(userCredentials);
  }

  @PostMapping(path = "/login", headers = "!Authorization")
  @ResponseStatus(HttpStatus.OK)
  JWTToken loginUser(@RequestBody @Valid UserCredentials userCredentials)
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
    return new JWTToken(token);
  }
}

class JWTToken {
  private String jwtToken;

  public JWTToken() {}

  public JWTToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }

  public String getJwtToken() {
    return jwtToken;
  }

  public void setJwtToken(String jwtToken) {
    this.jwtToken = jwtToken;
  }
}

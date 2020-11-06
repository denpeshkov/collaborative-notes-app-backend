package com.github.denpeshkov.authenticationservice.user;

import com.github.denpeshkov.authenticationservice.exception.IncorrectPasswordException;
import com.github.denpeshkov.authenticationservice.exception.UserAlreadyExistsException;
import com.github.denpeshkov.authenticationservice.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock UserRepository userRepository;
  // uses real BCryptPasswordEncoder
  @Spy BCryptPasswordEncoder passwordEncoder;
  @InjectMocks UserService userService;

  @Test
  void getUser() throws UserNotFoundException, IncorrectPasswordException {
    UserCredentials user = new UserCredentials("root", "root");

    when(userRepository.findByUsername("root"))
        .thenReturn(Optional.of(new UserCredentials("root", "root")));

    Assertions.assertEquals(userService.getUser(user.getUsername()), user);

    UserNotFoundException userNotFoundException =
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUser("root1"));
    Assertions.assertEquals(
        userNotFoundException.getMessage(), "User with given username: root1 is not found!");
  }

  @Test
  void createUser() throws UserAlreadyExistsException {
    when(userRepository.existsByUsername("root")).thenReturn(false);
    Assertions.assertDoesNotThrow(
        () -> userService.createUser(new UserCredentials("root", "root")));

    when(userRepository.existsByUsername("root")).thenReturn(true);
    UserAlreadyExistsException userAlreadyExistsException =
        Assertions.assertThrows(
            UserAlreadyExistsException.class,
            () -> userService.createUser(new UserCredentials("root", "root")));
    Assertions.assertEquals(
        userAlreadyExistsException.getMessage(),
        "Cannot create a user with given username: root! User with given username already exists!");
  }

  @Test
  void updateUser() throws UserNotFoundException {
    ArgumentCaptor<UserCredentials> userCredentialsArgumentCaptor =
        ArgumentCaptor.forClass(UserCredentials.class);

    when(userRepository.findByUsername("root"))
        .thenReturn(Optional.of(new UserCredentials("root", "root")));

    UserCredentials newUser = new UserCredentials("root", "new_root");

    userService.updateUser(newUser);

    verify(userRepository).save(userCredentialsArgumentCaptor.capture());

    // asserts that users are equal (username only)
    Assertions.assertEquals(userCredentialsArgumentCaptor.getValue(), newUser);

    Assertions.assertTrue(
        passwordEncoder.matches(
            newUser.getPassword(), userCredentialsArgumentCaptor.getValue().getPassword()));
  }

  @Test
  void deleteUser() {
    when(userRepository.findByUsername("root"))
        .thenReturn(Optional.of(new UserCredentials("root", "root")));
    Assertions.assertDoesNotThrow(() -> userService.deleteUser("root"));

    when(userRepository.findByUsername("root")).thenReturn(Optional.empty());
    UserNotFoundException userNotFoundException =
        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser("root"));
    Assertions.assertEquals(
        userNotFoundException.getMessage(), "User with given username: root is not found!");
  }

  @Test
  void verifyUser() {
    when(userRepository.findByUsername("root")).thenReturn(Optional.empty());

    Assertions.assertThrows(
        UserNotFoundException.class,
        () -> userService.verifyUser(new UserCredentials("root", "root")));

    String encodedPassword = passwordEncoder.encode("root");

    when(userRepository.findByUsername("root"))
        .thenReturn(Optional.of(new UserCredentials("root", encodedPassword)));

    Assertions.assertDoesNotThrow(
        () -> userService.verifyUser(new UserCredentials("root", "root")));

    encodedPassword = passwordEncoder.encode("root1");

    when(userRepository.findByUsername("root"))
        .thenReturn(Optional.of(new UserCredentials("root", encodedPassword)));

    Assertions.assertThrows(
        IncorrectPasswordException.class,
        () -> userService.verifyUser(new UserCredentials("root", "root")));
  }
}

package com.github.denpeshkov.authenticationservice.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock UserRepository userRepository;
  @Mock BCryptPasswordEncoder passwordEncoder;
  @InjectMocks UserService userService;

  @Test
  void loadUserByUsername() {
    User user = new User("root", "root", Collections.emptySet());
    when(userRepository.findByUsername("root")).thenReturn(new UserCredentials("root", "root"));

    Assertions.assertEquals(userService.loadUserByUsername("root"), user);
    UsernameNotFoundException usernameNotFoundException =
        Assertions.assertThrows(
            UsernameNotFoundException.class, () -> userService.loadUserByUsername("root1"));
    Assertions.assertEquals(
        usernameNotFoundException.getMessage(), "User with given username is not found: root1");
  }
}

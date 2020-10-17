package com.github.denpeshkov.authenticationservice.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.TestPropertySource;

import java.util.Set;

@DataJdbcTest
@TestPropertySource("/bootstrap.yml")
class UserRepositoryTestIT {

  @Autowired private UserRepository userRepository;

  @Test
  void findByUsernameWhenNoUserExists() {
    UserCredentials root = userRepository.findByUsername("root");
    Assertions.assertNull(root);
  }

  @Test
  void findByUsernameWhenUserExists() {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    userRepository.save(userCredentials);
    UserCredentials userCredentials1 = userRepository.findByUsername("root");
    Assertions.assertEquals(userCredentials, userCredentials1);

    userCredentials =
        new UserCredentials(
            "root1",
            "root1",
            Set.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("USER")));

    userRepository.save(userCredentials);
    userCredentials1 = userRepository.findByUsername("root1");
    Assertions.assertEquals(userCredentials, userCredentials1);
  }

  @Test
  void updateUser() {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    userRepository.save(userCredentials);
    UserCredentials userCredentials1 = userRepository.findByUsername("root");
    Assertions.assertEquals(userCredentials, userCredentials1);

    UserCredentials updatedUserCredentials = new UserCredentials("root", "root1");

    userRepository.updateUser(
        updatedUserCredentials.getUsername(), updatedUserCredentials.getPassword());
    Assertions.assertEquals(updatedUserCredentials, userRepository.findByUsername("root"));
  }
}

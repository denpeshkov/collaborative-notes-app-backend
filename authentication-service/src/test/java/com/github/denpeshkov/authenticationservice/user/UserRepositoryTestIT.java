package com.github.denpeshkov.authenticationservice.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@TestPropertySource("/bootstrap.yml")
class UserRepositoryTestIT {

  @Autowired private UserRepository userRepository;

  @Test
  void findByUsernameWhenNoUserExists() {
    Optional<UserCredentials> root = userRepository.findByUsername("root");
    assertTrue(root.isEmpty());
  }

  @Test
  void findByUsernameWhenUserExists() {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    userRepository.save(userCredentials);
    Optional<UserCredentials> userCredentials1 = userRepository.findByUsername("root");
    assertTrue(userCredentials1.isPresent());
    Assertions.assertEquals(userCredentials, userCredentials1.get());

    userCredentials = new UserCredentials("root1", "root1");

    userRepository.save(userCredentials);
    userCredentials1 = userRepository.findByUsername("root1");
    assertTrue(userCredentials1.isPresent());
    Assertions.assertEquals(userCredentials, userCredentials1.get());
  }

  @Test
  void updateUser() {
    UserCredentials userCredentials = new UserCredentials("root", "root");

    userRepository.save(userCredentials);
    Optional<UserCredentials> userCredentials1 = userRepository.findByUsername("root");
    assertTrue(userCredentials1.isPresent());
    Assertions.assertEquals(userCredentials, userCredentials1.get());

    UserCredentials updatedUserCredentials = new UserCredentials("root", "new_root");
    userCredentials.updateUser(updatedUserCredentials);

    userRepository.save(userCredentials);
    Assertions.assertEquals(updatedUserCredentials, userRepository.findByUsername("root").get());

    userCredentials1.get().updateUser(updatedUserCredentials);

    userRepository.save(userCredentials1.get());
    Assertions.assertEquals(updatedUserCredentials, userRepository.findByUsername("root").get());
  }

  @Test
  void parametersValidation() {
    assertThrows(IllegalArgumentException.class, () -> userRepository.save(null));
  }

  @Test
  void deleleteUser() {
    UserCredentials userCredentials = new UserCredentials("root", "root");
    userRepository.save(userCredentials);

    Assertions.assertTrue(userRepository.existsByUsername(userCredentials.getUsername()));

    userRepository.delete(userCredentials);

    assertFalse(userRepository.existsByUsername(userCredentials.getUsername()));
  }
}

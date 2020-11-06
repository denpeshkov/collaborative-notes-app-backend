package com.github.denpeshkov.authenticationservice.user;

import com.github.denpeshkov.authenticationservice.exception.IncorrectPasswordException;
import com.github.denpeshkov.authenticationservice.exception.UserAlreadyExistsException;
import com.github.denpeshkov.authenticationservice.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/** User's management for authentication and security */
@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  /**
   * Gets user by username
   *
   * @param username username
   * @return {@link Optional} of user with given username
   * @throws UserNotFoundException if user with given username doesn't exist
   */
  public UserCredentials getUser(String username) throws UserNotFoundException {
    Optional<UserCredentials> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty())
      throw new UserNotFoundException(
          String.format("User with given username: %s is not found!", username));

    return userOptional.get();
  }

  /**
   * Verifies that user with given username and password exists
   *
   * @param userCredentials user's credentials
   * @throws UserNotFoundException if user with given username is not found
   * @throws IncorrectPasswordException if password is incorrect
   */
  public void verifyUser(UserCredentials userCredentials)
      throws UserNotFoundException, IncorrectPasswordException {
    UserCredentials user = getUser(userCredentials.getUsername());

    if (!bCryptPasswordEncoder.matches(userCredentials.getPassword(), user.getPassword()))
      throw new IncorrectPasswordException(
          String.format(
              "Incorrect password for user with username: %s!", userCredentials.getUsername()));
  }

  /**
   * Creates a new user if user with given username doesn't already exists
   *
   * @param userCredentials user credential's
   * @throws UserAlreadyExistsException if user with given username already exists
   */
  // TODO transactions
  public void createUser(UserCredentials userCredentials) throws UserAlreadyExistsException {
    if (userExists(userCredentials.getUsername()))
      throw new UserAlreadyExistsException(
          String.format(
              "Cannot create a user with given username: %s! User with given username already exists!",
              userCredentials.getUsername()));

    // encrypt password prior to saving to database
    userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

    userRepository.save(userCredentials);
  }

  /**
   * Updates user
   *
   * @param userCredentials new user credential's
   */
  public void updateUser(UserCredentials userCredentials) throws UserNotFoundException {
    // copy of userCredentials
    UserCredentials newUser = new UserCredentials(userCredentials);

    Optional<UserCredentials> userToUpdate =
        userRepository.findByUsername(userCredentials.getUsername());

    if (userToUpdate.isEmpty())
      throw new UserNotFoundException(
          String.format(
              "User with given username: %s is not found!", userCredentials.getUsername()));

    // encrypt password prior to saving to database
    newUser.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

    userToUpdate.get().updateUser(newUser);

    userRepository.save(userToUpdate.get());
  }

  /**
   * Deletes given user
   *
   * @param username username of user to be deleted
   */
  public void deleteUser(String username) throws UserNotFoundException {
    Optional<UserCredentials> userCredentials = userRepository.findByUsername(username);

    if (userCredentials.isEmpty())
      throw new UserNotFoundException(
          String.format("User with given username: %s is not found!", username));

    userRepository.delete(userCredentials.get());
  }

  /**
   * Checks if user with given username already exists
   *
   * @param username username
   * @return {@code true} if user with given username already exists, else {@code false}
   */
  public boolean userExists(String username) {
    return userRepository.existsByUsername(username);
  }
}

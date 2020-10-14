package com.github.denpeshkov.authenticationservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

/** Loads and creates user-specific authentication (security) data */
@Service
public class UserService implements UserDetailsManager {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public void createUser(UserDetails user) {
    UserCredentials userCredentials = new UserCredentials(user);

    // encrypt password prior to saving to database
    userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

    userRepository.save(userCredentials);
  }

  /**
   * Updates user's password
   *
   * @param user user with new password
   */
  @Override
  public void updateUser(UserDetails user) {
    UserCredentials userCredentials = new UserCredentials(user);

    // encrypt password prior to saving to database
    userCredentials.setPassword(bCryptPasswordEncoder.encode(userCredentials.getPassword()));

    userRepository.updateUser(userCredentials.getUsername(), userCredentials.getPassword());
  }

  @Override
  public void deleteUser(String username) {
    UserCredentials userCredentials = userRepository.findByUsername(username);

    userRepository.delete(userCredentials);
  }

  /**
   * Use {@link #updateUser(UserDetails)} instead
   *
   * @param oldPassword
   * @param newPassword
   */
  @Override
  public void changePassword(String oldPassword, String newPassword) {}

  /**
   * {@inheritDoc}
   *
   * <p><b> always returns {@code true} at the moment !!!
   *
   * @param username
   * @return
   */
  @Override
  public boolean userExists(String username) {
    return true;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserCredentials userCredentials = userRepository.findByUsername(username);

    if (userCredentials == null)
      throw new UsernameNotFoundException("User with given username is not found: " + username);

    return new User(
        userCredentials.getUsername(),
        userCredentials.getPassword(),
        userCredentials.getAuthorities());
  }
}

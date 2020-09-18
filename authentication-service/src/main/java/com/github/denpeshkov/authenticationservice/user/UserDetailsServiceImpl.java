package com.github.denpeshkov.authenticationservice.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Gets user's from database prior to authentication */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final BCryptPasswordEncoder encoder;

  @Autowired
  public UserDetailsServiceImpl(BCryptPasswordEncoder encoder) {
    this.encoder = encoder;
  }

  /**
   * Loads user info from DB
   *
   * @param username username
   * @return user's info
   * @throws UsernameNotFoundException exception thrown if user's not found
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    // hard coding the users. All passwords must be encoded.
    final List<User> users =
        Arrays.asList(
            new User(
                "user",
                encoder.encode("12345"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"))),
            new User(
                "admin",
                encoder.encode("12345"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))));

    for (UserDetails user : users) {
      if (user.getUsername().equals(username)) return user;
    }

    // If user not found. Throw this exception.
    throw new UsernameNotFoundException("Username: " + username + " not found");
  }
}

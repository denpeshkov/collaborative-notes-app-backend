package com.github.denpeshkov.authenticationservice.user;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserCredentials, Long> {
  /**
   * Finds a user by username
   *
   * @param username username
   * @return user with given username
   */
  @Query("select * from USER_CREDENTIALS where username = :username")
  UserCredentials findByUsername(String username);

  // modifying query
  @Modifying
  @Query("update USER_CREDENTIALS set password = :password where username = :username")
  int updateUser(String username, String password);
}

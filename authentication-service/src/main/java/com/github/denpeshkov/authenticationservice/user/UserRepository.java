package com.github.denpeshkov.authenticationservice.user;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserCredentials, Long> {
  /**
   * Finds a user by username
   *
   * @param username must not be {@literal null}
   * @return user with given username
   */
  @Query("select * from USER_CREDENTIALS where username = :username")
  Optional<UserCredentials> findByUsername(String username);

  /**
   * Returns whether an user with the given username exists
   *
   * @param username must not be {@literal null}
   * @return {@literal true} if an user with the given username exists, {@literal false} otherwise
   */
  @Query("select (case when exists (select 1 from USER_CREDENTIALS where username = :username) then true else false end)")
  boolean existsByUsername(String username);
}

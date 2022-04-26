package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {

  Optional<User> findUserByUsernameAndPassword(String username, String password);

  List<User> getAllUsers();
  Optional<User> findUserById(long id);
  Optional<User> findUserByEmail(String email);
  int save(User user);
  // todo this should not return int (the affected rows) but probably the newly
  // created user.

  int deleteById(long id);
  // todo should this return int (the affected rows)?


}

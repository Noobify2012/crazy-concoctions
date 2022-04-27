package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.UserDto;
import com.concoctions.concoctionsbackend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {

  Optional<User> findUserByUsernameAndPassword(String username, String password);

  List<User> getAll();
  Optional<User> getById(long userId);
  Optional<User> getByEmail(String email);
  Optional<User> update(long userId, UserDto userDto);
  Optional<User> save(UserDto userDto);
  // todo this should not return int (the affected rows) but probably the newly
  // created user.

  int deleteById(long id);
  // todo should this return int (the affected rows)?


}

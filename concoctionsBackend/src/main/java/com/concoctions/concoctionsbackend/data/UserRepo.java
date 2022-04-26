package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
  List<User> getAllUsers();
  Optional<User> findUserById(long id);

  Optional<User> findUserByEmail(String email);

  int save(User user);
}

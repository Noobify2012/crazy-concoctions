package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.User;

import java.util.List;

public interface UserRepo {
  List<User> getAllUsers();
  User findUserById(long id);

  int save(User user);
}

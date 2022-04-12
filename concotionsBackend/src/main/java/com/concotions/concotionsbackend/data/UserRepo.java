package com.concotions.concotionsbackend.data;

import com.concotions.concotionsbackend.model.User;

import java.util.List;

public interface UserRepo {
  List<User> getAllUsers();
}

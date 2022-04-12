package com.concotions.concotionsbackend.controller;

import com.concotions.concotionsbackend.data.UserRepo;
import com.concotions.concotionsbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserRepo userRepo;

  @Autowired
  public UserController(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  @GetMapping("/all")
  public List<User> allUsers() {
    return userRepo.getAllUsers();
  }
}

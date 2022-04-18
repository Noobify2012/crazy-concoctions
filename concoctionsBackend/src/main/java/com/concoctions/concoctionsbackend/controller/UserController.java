package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.UserRepo;
import com.concoctions.concoctionsbackend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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

  @GetMapping("/find")
  public User findUserById(
      @RequestParam Long id
  ){
    return userRepo.findUserById(id);
  }

  @PostMapping
  public ResponseEntity<User> createUser(
      @RequestBody User user
  ) {
    log.info("{}", user);
    int linesAffected = userRepo.save(user);
    log.info("lines effected: {}", linesAffected);

    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(user);

  }
}

package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.UserRepo;
import com.concoctions.concoctionsbackend.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  public ResponseEntity<List<User>> allUsers() {
    List<User> users = userRepo.getAllUsers();
    return ResponseEntity
        .ok()
        .body(users);
  }

  @GetMapping("/find/{userId}")
  public ResponseEntity<User> findUserById(
      @PathVariable long userId
  ){
    return userRepo.findUserById(userId)
        .map(value -> ResponseEntity
          .ok()
          .body(value))
        .orElseGet(() -> ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(null)
        );
  }

  @PostMapping
  public ResponseEntity<User> createUser(
      @RequestBody User user
  ) {
    log.info("{}", user);
    int linesAffected = userRepo.save(user);
    log.info("lines effected: {}", linesAffected);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .body(null);
    //todo do I want to return the newly created user?

  }

  @DeleteMapping("/delete/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(
      @PathVariable long userId
  ){
    userRepo.deleteById(userId);
  }
}

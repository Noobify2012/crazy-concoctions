package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.UserRepo;
import com.concoctions.concoctionsbackend.dto.LoginUser;
import com.concoctions.concoctionsbackend.dto.User;
import com.concoctions.concoctionsbackend.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/login")
public class StupidLoginController {

//  private final UserRepo userRepo;
  private final LoginService loginService;

  @Autowired
  public StupidLoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping
  public ResponseEntity<User> stupidLogin(
      @RequestBody LoginUser loginUser
  ){
    Optional<User> user = loginService.login(loginUser);
    return user.map(result -> ResponseEntity
            .ok()
            .body(result))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.UNAUTHORIZED).build()
        );
  }
}

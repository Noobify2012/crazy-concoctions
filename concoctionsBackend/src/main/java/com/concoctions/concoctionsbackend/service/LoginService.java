package com.concoctions.concoctionsbackend.service;

import com.concoctions.concoctionsbackend.data.UserRepo;
import com.concoctions.concoctionsbackend.dto.LoginUserDto;
import com.concoctions.concoctionsbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

  private final UserRepo userRepo;

  @Autowired
  public LoginService(UserRepo userRepo) {
    this.userRepo = userRepo;
  }

  public Optional<User> login(LoginUserDto loginUserDto) {
    return userRepo
        .findUserByUsernameAndPassword(
            loginUserDto.getUsername(),
            loginUserDto.getPassword()
        );

  }
}

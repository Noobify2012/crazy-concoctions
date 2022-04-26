package com.concoctions.concoctionsbackend.dto;

import lombok.Data;

@Data
public class UserDto {
  private String email;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String bio;
}

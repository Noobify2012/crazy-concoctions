package com.concoctions.concoctionsbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
  long userId;
  String email;
  String username;
  String password;
  String firstName;
  String lastName;
  String bio;
}

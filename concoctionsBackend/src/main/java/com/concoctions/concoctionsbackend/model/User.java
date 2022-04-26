package com.concoctions.concoctionsbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
  private long userId;
  private String email;
  private String username;
//  private String password;
  private String firstName;
  private String lastName;
  private String bio;
}

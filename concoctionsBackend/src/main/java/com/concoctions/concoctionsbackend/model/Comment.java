package com.concoctions.concoctionsbackend.model;

import lombok.Data;

@Data
public class Comment {
  long commentID;
  User user;
  Drink drink;
  int ranking;
  String commentBody;
}

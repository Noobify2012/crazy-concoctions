package com.concoctions.concoctionsbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
  long commentId;
  long userId;
  long drinkId;
  int ranking;
  String commentBody;
}

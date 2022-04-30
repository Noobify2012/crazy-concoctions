package com.concoctions.model;

import lombok.Builder;
import lombok.Data;

@Data
public class Comment {
  private long commentId;
  private long userId;
  private long drinkId;
  private int ranking;
  private String commentBody;
}

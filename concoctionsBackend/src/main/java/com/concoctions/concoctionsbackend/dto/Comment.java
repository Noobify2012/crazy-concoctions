package com.concoctions.concoctionsbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Comment {
  private long commentId;
  private long userId;
  private long drinkId;
  private int ranking;
  private String commentBody;
}

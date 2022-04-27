package com.concoctions.concoctionsbackend.dto;

import lombok.Data;

@Data
public class CommentDto {
  private long userId;
  private long drinkId;
  private int ranking;
  private String commentBody;

}

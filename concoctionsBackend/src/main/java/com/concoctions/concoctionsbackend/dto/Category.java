package com.concoctions.concoctionsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
  private long categoryId;
  private String name;
  private String description;
}

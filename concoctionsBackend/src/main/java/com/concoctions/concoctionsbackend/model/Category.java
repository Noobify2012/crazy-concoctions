package com.concoctions.concoctionsbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Category {
  private long categoryId;
  private String name;
  private String description;
}

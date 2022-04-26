package com.concoctions.concoctionsbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ingredient {
  private long ingredientId;
  private String name;
  private Type type;
  private String description;
}

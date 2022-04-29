package com.concoctions.concoctionsbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ingredient {
  private long ingredientId;
  private String name;
  private Type type;
  private String description;
  private boolean isAlcoholic;

}

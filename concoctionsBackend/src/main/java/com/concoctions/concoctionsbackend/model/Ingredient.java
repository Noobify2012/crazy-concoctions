package com.concoctions.concoctionsbackend.model;

import com.concoctions.concoctionsbackend.dto.IngredientDto;
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

package com.concoctions.concoctionsbackend.dto;

import lombok.Data;

@Data
public class IngredientDto {
  private String name;
  private String typeId;
  private String description;
  private boolean isAlcoholic;
}

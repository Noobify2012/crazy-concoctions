package com.concoctions.model;

import lombok.Data;

@Data
public class DrinkIngredientDto {
  private long ingredientId;
  private long uomId;
  private double amount;
}

package com.concoctions.concoctionsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrinkIngredientDto {
  private long ingredientId;
  private long uomId;
  private double amount;
}

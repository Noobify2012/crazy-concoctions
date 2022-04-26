package com.concoctions.concoctionsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrinkIngredient {
  private UnitOfMeasure uom;
  private Ingredient ingredient;
  private double amount;
}

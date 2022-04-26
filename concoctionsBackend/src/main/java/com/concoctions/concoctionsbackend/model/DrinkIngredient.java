package com.concoctions.concoctionsbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrinkIngredient {
  private UnitOfMeasure uom;
  private Ingredient ingredient;
  private double amount;
}

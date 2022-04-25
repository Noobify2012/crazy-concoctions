package com.concoctions.concoctionsbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DrinkIngredient {
  UnitOfMeasure uom;
  Ingredient ingredient;
  double amount;
}

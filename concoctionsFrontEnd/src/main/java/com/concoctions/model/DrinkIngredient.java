package com.concoctions.model;

import lombok.Data;

@Data
public class DrinkIngredient {
  Uom uom;
  Ingredient ingredient;
  double amount;
}

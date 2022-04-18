package com.concoctions.concoctionsbackend.model;

import lombok.Data;

@Data
public class IngredientAmount {
  UnitOfMeasure uom;
  Ingredient ingredient;
  double amount;
}

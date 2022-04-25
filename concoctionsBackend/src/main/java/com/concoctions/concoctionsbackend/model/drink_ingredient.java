package com.concoctions.concoctionsbackend.model;

import lombok.Data;

@Data
public class drink_ingredient {
  UnitOfMeasure uom;
  Ingredient ingredient;
  double amount;
}

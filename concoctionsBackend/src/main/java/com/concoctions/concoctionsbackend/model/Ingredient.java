package com.concoctions.concoctionsbackend.model;

import lombok.Data;

@Data
public class Ingredient {
  long ingredientId;
  String name;
  String type;
  String description;
}

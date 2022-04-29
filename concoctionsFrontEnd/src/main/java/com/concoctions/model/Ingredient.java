package com.concoctions.model;

import lombok.Data;

@Data
public class Ingredient {
  long ingredientId;
  String name;
  Type type;
  String description;
  boolean alcoholic;



}

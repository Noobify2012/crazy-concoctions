package com.concoctions.concoctionsbackend.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ingredient {
  long ingredientId;
  String name;
  String type;
  String description;
}

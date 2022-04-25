package com.concoctions.concoctionsbackend.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Drink {
  long drinkId;
  String name;
  Category category;
  boolean isHot;
  String description;
  List<drink_ingredient> ingredients;
  List<FoodItem> pairings;

}

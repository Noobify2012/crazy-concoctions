package com.concoctions.concoctionsbackend.model;

import lombok.Data;

import java.util.List;

@Data
public class Drink {
  long drinkId;
  String name;
  Category category;
  boolean isHot;
  String description;
  List<IngredientAmount> ingredients;
  List<FoodItem> pairings;

}

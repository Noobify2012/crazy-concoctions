package com.concoctions.model;

import lombok.Data;

import java.util.List;

@Data
public class Drink {
  long drinkId;
  long userId;
  String name;
  Category category;
  String description;
  List<DrinkIngredient> drinkIngredients;
  List<FoodItem> pairings;
  boolean hot;
}

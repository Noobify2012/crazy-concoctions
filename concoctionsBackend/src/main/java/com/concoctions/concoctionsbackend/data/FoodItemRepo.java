package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.FoodItem;

import java.util.List;

public interface FoodItemRepo {
  List<FoodItem> getAllFoodItems();
  List<FoodItem> getAllFoodItemsByDrinkId(long id);
  FoodItem getFoodItemById(long id);

}

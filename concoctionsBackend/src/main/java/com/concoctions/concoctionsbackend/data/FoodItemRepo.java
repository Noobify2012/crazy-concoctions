package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.FoodItem;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepo {
  List<FoodItem> getAllFoodItems();
  List<FoodItem> getAllFoodItemsByDrinkId(long foodItemId);
  Optional<FoodItem> getFoodItemById(long foodItemId);
  int deleteFoodItemById(long foodItemId);

}

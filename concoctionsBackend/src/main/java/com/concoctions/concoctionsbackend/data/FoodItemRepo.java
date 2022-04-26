package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.FoodItem;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepo {
  List<FoodItem> getAllFoodItems();
  List<FoodItem> getAllFoodItemsByDrinkId(long id);
  Optional<FoodItem> getFoodItemById(long id);

}

package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.FoodItemDto;
import com.concoctions.concoctionsbackend.model.FoodItem;

import java.util.List;
import java.util.Optional;

public interface FoodItemRepo {
  List<FoodItem> getAll();
  List<FoodItem> getAllByDrinkId(long drinkId);
  Optional<FoodItem> getById(long foodItemId);
  Optional<FoodItem> save(FoodItemDto foodItemDto);
  List<FoodItem> saveAllByDrinkId(long drinkId, List<Long> foodItemIds);
  int deleteFoodItemById(long foodItemId);

}

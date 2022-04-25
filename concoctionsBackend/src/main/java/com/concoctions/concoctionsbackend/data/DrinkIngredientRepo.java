package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.DrinkIngredient;

import java.util.List;

public interface DrinkIngredientRepo {
  List<DrinkIngredient> getAllDrinkIngredientForDrinkId(long id);

}

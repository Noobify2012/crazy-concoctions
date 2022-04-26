package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.DrinkIngredientDto;
import com.concoctions.concoctionsbackend.model.DrinkIngredient;

import java.util.List;

public interface DrinkIngredientRepo {
  List<DrinkIngredient> getAllForDrinkId(long id);
  List<DrinkIngredient> saveAll(List<DrinkIngredientDto> drinkIngredientDtos);

}

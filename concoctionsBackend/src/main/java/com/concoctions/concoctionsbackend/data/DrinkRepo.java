package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.Drink;

import java.util.List;
import java.util.Optional;

public interface DrinkRepo {
  List<Drink> getAllDrinks();
  Optional<Drink> findDrinkById(long drinkId);
  int deleteDrinkById(long drinkId);
}

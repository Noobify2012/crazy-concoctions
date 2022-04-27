package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.DrinkDto;
import com.concoctions.concoctionsbackend.model.Drink;

import java.util.List;
import java.util.Optional;

public interface DrinkRepo {
  List<Drink> getAll();
  Optional<Drink> getById(long drinkId);
  Optional<Drink> save(DrinkDto drinkDto);
  Optional<Drink> update(long drinkId, DrinkDto drinkDto);
  int deleteDrinkById(long drinkId);
}

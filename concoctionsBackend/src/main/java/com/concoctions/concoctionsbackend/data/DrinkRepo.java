package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.DrinkDto;
import com.concoctions.concoctionsbackend.model.Drink;

import java.util.List;
import java.util.Optional;

public interface DrinkRepo {
  List<Drink> getAll();
  Optional<Drink> getById(long drinkId);
  List<Drink> getAllByName(String drinkName);
  List<Drink> getAllByUserId(long userId);
  List<Drink> getAlByCategoryId(long categoryId);
  Optional<Drink> save(DrinkDto drinkDto);
  Optional<Drink> update(long drinkId, DrinkDto drinkDto);
  int deleteDrinkById(long drinkId);

  List<Drink> getByCategoryName(String categoryName);
}

package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Drink;

import java.util.List;
import java.util.Optional;

public interface DrinkRepo {
  List<Drink> getAllDrinks();
  Optional<Drink> findDrinkById(Long id);
}

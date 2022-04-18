package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Drink;

import java.util.List;

public interface DrinkRepo {
  List<Drink> getAllDrinks();
}

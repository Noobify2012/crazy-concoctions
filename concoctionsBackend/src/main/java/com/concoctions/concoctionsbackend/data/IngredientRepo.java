package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepo {

  List<Ingredient> getAllIngredients();
  Optional<Ingredient> getIngredientById(long id);
}

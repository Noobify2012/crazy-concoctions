package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Ingredient;

import java.util.List;

public interface IngredientRepo {

  List<Ingredient> getAllIngredients();
  Ingredient getIngredientById(long id);
}

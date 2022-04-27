package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.IngredientDto;
import com.concoctions.concoctionsbackend.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepo {

  List<Ingredient> getAll();
  Optional<Ingredient> getById(long ingredientId);
  Optional<Ingredient> save(IngredientDto ingredientDto);
  Optional<Ingredient> update(long ingredientId, IngredientDto ingredientDto);
  int deleteById(long ingredientId);
}

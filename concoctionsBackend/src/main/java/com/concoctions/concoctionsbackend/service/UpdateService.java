package com.concoctions.concoctionsbackend.service;

import com.concoctions.concoctionsbackend.data.TypeRepo;
import com.concoctions.concoctionsbackend.dto.IngredientDto;
import com.concoctions.concoctionsbackend.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;

public class UpdateService {

  private final TypeRepo typeRepo;

  @Autowired
  public UpdateService(TypeRepo typeRepo) {
    this.typeRepo = typeRepo;
  }

//  public boolean update(Ingredient ingredient, IngredientDto ingredientDto) {
//    boolean update = false;
//
//  }
}

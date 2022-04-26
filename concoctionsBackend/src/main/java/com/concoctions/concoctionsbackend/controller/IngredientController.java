package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.IngredientRepo;
import com.concoctions.concoctionsbackend.dto.IngredientDto;
import com.concoctions.concoctionsbackend.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {

  private final IngredientRepo ingredientRepo;

  @Autowired
  public IngredientController(IngredientRepo ingredientRepo) {
    this.ingredientRepo = ingredientRepo;
  }


  @GetMapping("/all")
  public ResponseEntity<List<Ingredient>> allIngredients() {
    List<Ingredient> ingredients = ingredientRepo.getAll();
    return ResponseEntity.ok(ingredients);
  }

  @PostMapping("/save")
  public ResponseEntity<Ingredient> saveIngredient(
      @RequestBody IngredientDto ingredientDto
  ){
    Optional<Ingredient> ingredient = ingredientRepo.save(ingredientDto);
    return ingredient.map(value -> ResponseEntity
            .ok()
            .body(value))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{ingredientId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteIngredientById(
      @PathVariable long ingredientId
  ){
    ingredientRepo.deleteById(ingredientId);
  }

}

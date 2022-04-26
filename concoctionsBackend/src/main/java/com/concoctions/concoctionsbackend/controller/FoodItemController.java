package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.FoodItemRepo;
import com.concoctions.concoctionsbackend.dto.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/fooditem")
public class FoodItemController {

  private final FoodItemRepo foodItemRepo;

  @Autowired
  public FoodItemController(FoodItemRepo foodItemRepo) {
    this.foodItemRepo = foodItemRepo;
  }

  @GetMapping("/all")
  public ResponseEntity<List<FoodItem>> allFoodItems() {
    List<FoodItem> foodItems = foodItemRepo.getAllFoodItems();
    return ResponseEntity
        .ok()
        .body(foodItems);
  }

  @GetMapping("/find/{foodItemId}")
  public ResponseEntity<FoodItem> findFoodItemById(
      @PathVariable long foodItemId
  ){
    return foodItemRepo.getFoodItemById(foodItemId)
        .map(value -> ResponseEntity
            .ok()
            .body(value))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{foodItemId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteFoodItem(
      @PathVariable long foodItemId
  ){
    foodItemRepo.deleteFoodItemById(foodItemId);
  }
}

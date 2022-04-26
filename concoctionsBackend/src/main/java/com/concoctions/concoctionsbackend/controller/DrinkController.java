package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.DrinkRepo;
import com.concoctions.concoctionsbackend.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/drinks")
public class DrinkController {

  private final DrinkRepo drinkRepo;

  @Autowired
  public DrinkController(DrinkRepo drinkRepo) {
    this.drinkRepo = drinkRepo;
  }

  @GetMapping("/all")
  public List<Drink> allDrinks() {
    return drinkRepo.getAllDrinks();
  }

  @GetMapping("/find")
  public Drink findDrinkById(
      @RequestParam Long id
  ){
    return drinkRepo.findDrinkById(id);
  }

  @PostMapping
  public ResponseEntity<Drink> createDrink(
      @RequestBody Drink drink
  ) {
    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(drink);
  }

}

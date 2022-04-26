package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.DrinkRepo;
import com.concoctions.concoctionsbackend.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    return drinkRepo.getAll();
  }

  @GetMapping("/find")
  public ResponseEntity<Drink> findDrinkById(
      @RequestParam Long id
  ){
    Optional<Drink> drink = drinkRepo.findById(id);
    return drink
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @PostMapping
  public ResponseEntity<Drink> createDrink(
      @RequestBody Drink drink
  ) {
    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(drink);
    //todo you actually need to implement this shit. . .
  }

  @DeleteMapping("/delete/{drinkId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDrinkById(
      @PathVariable long drinkId
  ){
    drinkRepo.deleteDrinkById(drinkId);
  }

}

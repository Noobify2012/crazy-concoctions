package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.DrinkRepo;
import com.concoctions.concoctionsbackend.dto.DrinkDto;
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
    Optional<Drink> drink = drinkRepo.getById(id);
    return drink
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @PostMapping("/save")
  public ResponseEntity<Drink> saveDrink(
      @RequestBody DrinkDto drinkDto
  ) {
    Optional<Drink> drink = drinkRepo.save(drinkDto);
    return drink.map(value -> ResponseEntity
            .ok()
            .body(value))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{drinkId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteDrinkById(
      @PathVariable long drinkId
  ){
    drinkRepo.deleteDrinkById(drinkId);
  }

}

package com.concoctions.concoctionsbackend.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Drink {
  private long drinkId;
  private long userId;
  private String name;
  private Category category;
  private boolean isHot;
  private String description;
  private List<DrinkIngredient> drinkIngredients;
  private List<FoodItem> pairings;

}

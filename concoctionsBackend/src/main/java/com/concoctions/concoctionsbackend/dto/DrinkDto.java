package com.concoctions.concoctionsbackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class DrinkDto {
  private long userId;
  private String name;
  private long categoryId;
  private boolean isHot;
  private String description;
  private List<DrinkIngredientDto> drinkIngredientDtos;
  private List<Long> foodItemIds;
}

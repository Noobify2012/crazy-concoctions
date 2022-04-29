package com.concoctions.model;

import lombok.Data;

import java.util.List;
@Data
public class NewDrink {
    private long userId;
    private String name;
    private long categoryId;
    private boolean isHot;
    private String description;
    private List<DrinkIngredient> drinkIngredients;
    private List<Long> foodItemIds;
}

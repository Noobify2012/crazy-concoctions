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
    //fix this
    private List<DrinkIngredientDto> drinkIngredients;
    private List<Long> foodItemIds;
}

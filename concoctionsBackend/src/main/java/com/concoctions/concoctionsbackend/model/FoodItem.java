package com.concoctions.concoctionsbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FoodItem {
  private long foodItemId;
  private String name;
}

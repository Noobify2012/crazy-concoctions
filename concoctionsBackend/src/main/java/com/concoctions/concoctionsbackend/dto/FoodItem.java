package com.concoctions.concoctionsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FoodItem {
  private long foodItemId;
  private String name;
}

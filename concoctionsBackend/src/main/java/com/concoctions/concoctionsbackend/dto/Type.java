package com.concoctions.concoctionsbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Type {
  private long typeID;
  private String name;
  private String description;

}

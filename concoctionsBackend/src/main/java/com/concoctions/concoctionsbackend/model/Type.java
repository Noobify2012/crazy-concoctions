package com.concoctions.concoctionsbackend.model;

import com.concoctions.concoctionsbackend.dto.TypeDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Type {

  private long typeId;
  private String name;
  private String description;


  public boolean update(TypeDto typeDto) {
    boolean updated = false;

    if (typeDto.getName() != null) {
      this.setName(typeDto.getName());
      updated = true;
    }
    if (typeDto.getDescription() != null) {
      this.setDescription(typeDto.getDescription());
      updated = true;
    }

    return updated;
  }

}

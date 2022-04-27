package com.concoctions.concoctionsbackend.model;

import com.concoctions.concoctionsbackend.dto.UomDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnitOfMeasure {
  private long uomId;
  private String name;
  private String type;

}

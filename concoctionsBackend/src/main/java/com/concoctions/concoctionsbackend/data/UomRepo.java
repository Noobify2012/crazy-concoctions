package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.UomDto;
import com.concoctions.concoctionsbackend.model.UnitOfMeasure;

import java.util.List;
import java.util.Optional;

public interface UomRepo {

  List<UnitOfMeasure> getAll();
  Optional<UnitOfMeasure> getById(long uomId);
  Optional<UnitOfMeasure> getByName(String name);
  Optional<UnitOfMeasure> save(UomDto uomDto);
  int deleteById(long uomId);

}
package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.UnitOfMeasure;

import java.util.List;
import java.util.Optional;

public interface UomRepo {

  List<UnitOfMeasure> getAllUoms();

  Optional<UnitOfMeasure> getUomById(long id);

  Optional<UnitOfMeasure> getUomByName(String name);

}

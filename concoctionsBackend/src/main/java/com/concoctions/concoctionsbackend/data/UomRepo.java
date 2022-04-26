package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.UnitOfMeasure;

import java.util.List;

public interface UomRepo {

  List<UnitOfMeasure> getAllUoms();

  UnitOfMeasure getUomById(long id);

  UnitOfMeasure getUomByName(String name);

}

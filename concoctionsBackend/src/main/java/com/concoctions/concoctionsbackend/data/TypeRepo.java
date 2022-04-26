package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Type;

import java.util.List;

public interface TypeRepo {

  List<Type> getAllTypes();
  Type getTypeById(long id);
  Type getTypeByName(String name);
}

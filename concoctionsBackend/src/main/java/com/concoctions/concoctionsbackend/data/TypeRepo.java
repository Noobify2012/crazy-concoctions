package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Type;

import java.util.List;
import java.util.Optional;

public interface TypeRepo {

  List<Type> getAllTypes();
  Optional<Type> getTypeById(long id);
  Optional<Type> getTypeByName(String name);
}

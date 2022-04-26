package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.Type;

import java.util.List;
import java.util.Optional;

public interface TypeRepo {

  List<Type> getAllTypes();
  Optional<Type> getTypeById(long typeId);
  Optional<Type> getTypeByName(String name);
  int deleteById(long typeId);
}

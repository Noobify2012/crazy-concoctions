package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.TypeDto;
import com.concoctions.concoctionsbackend.model.Type;

import java.util.List;
import java.util.Optional;

public interface TypeRepo {

  List<Type> getAll();
  Optional<Type> getById(long typeId);
  Optional<Type> getByName(String name);
  Optional<Type> save(TypeDto typeDto);
  int deleteById(long typeId);
}

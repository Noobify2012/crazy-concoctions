package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.TypeRepo;
import com.concoctions.concoctionsbackend.dto.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/type")
public class TypeController {

  private final TypeRepo typeRepo;

  @Autowired
  public TypeController(TypeRepo typeRepo) {
    this.typeRepo = typeRepo;
  }

  @GetMapping("/all")
  public ResponseEntity<List<Type>> allTypes() {
    List<Type> types = typeRepo.getAllTypes();
    return ResponseEntity.ok(types);
  }

  @GetMapping("/find/{typeId}")
  public ResponseEntity<Type> findTypeById(
      @PathVariable long typeId
  ){
    return typeRepo.getTypeById(typeId)
        .map(value -> ResponseEntity
            .ok()
            .body(value))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{typeId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteType(
      @PathVariable long typeId
  ) {
    typeRepo.deleteById(typeId);
  }
}

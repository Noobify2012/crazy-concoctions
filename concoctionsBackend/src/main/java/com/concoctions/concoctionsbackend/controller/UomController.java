package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.model.UnitOfMeasure;
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
@RequestMapping("/uom")
public class UomController {

  private final UomRepo uomRepo;

  @Autowired
  public UomController(UomRepo uomRepo) {
    this.uomRepo = uomRepo;
  }

  @GetMapping("/all")
  public ResponseEntity<List<UnitOfMeasure>> getAllUom() {
    List<UnitOfMeasure> uoms = uomRepo.getAllUoms();
    return ResponseEntity.ok(uoms);
  }

  @GetMapping("/find/{uomId}")
  public ResponseEntity<UnitOfMeasure> findUomById(
      @PathVariable long uomId
  ){
    return uomRepo.getUomById(uomId)
        .map(value -> ResponseEntity
            .ok()
            .body(value)
        )
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{uomId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUomById(
      @PathVariable long uomId
  ){
    uomRepo.deleteUomById(uomId);
  }
}

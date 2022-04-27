package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.dto.UomDto;
import com.concoctions.concoctionsbackend.model.UnitOfMeasure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    List<UnitOfMeasure> uoms = uomRepo.getAll();
    return ResponseEntity.ok(uoms);
  }

  @GetMapping("/find/{uomId}")
  public ResponseEntity<UnitOfMeasure> findUomById(
      @PathVariable long uomId
  ){
    return uomRepo.getById(uomId)
        .map(value -> ResponseEntity
            .ok()
            .body(value)
        )
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @PostMapping("/save")
  public ResponseEntity<UnitOfMeasure> saveUom(
      @RequestBody UomDto uomDto
  ){
    Optional<UnitOfMeasure> uom = uomRepo.save(uomDto);
    return uom.map(value -> ResponseEntity
            .ok()
            .body(value))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{uomId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUomById(
      @PathVariable long uomId
  ){
    uomRepo.deleteById(uomId);
  }
}

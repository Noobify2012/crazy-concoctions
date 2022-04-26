package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.CategoryRepo;
import com.concoctions.concoctionsbackend.dto.Category;
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
@RequestMapping("/category")
public class CategoryController {

  private final CategoryRepo categoryRepo;

  @Autowired
  public CategoryController(CategoryRepo categoryRepo){
    this.categoryRepo = categoryRepo;
  }

  @GetMapping("/all")
  public ResponseEntity<List<Category>> getAllCategories() {
    List<Category> categories = categoryRepo.getAllCategories();
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/find/{categoryId}")
  public ResponseEntity<Category> getCategoryById(
      @PathVariable long categoryId
  ){
    return categoryRepo.getCategoryById(categoryId)
        .map(value -> ResponseEntity
            .ok()
            .body(value))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{categoryId}")
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public void deleteCategory(
      @PathVariable long categoryId
  ){
    categoryRepo.deleteCategoryById(categoryId);
  }

}

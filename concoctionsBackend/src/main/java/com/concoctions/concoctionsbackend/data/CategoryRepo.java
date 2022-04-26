package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.CategoryDto;
import com.concoctions.concoctionsbackend.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo {
  List<Category> getAll();
  Optional<Category> getById(long categoryId);
  Optional<Category> save(CategoryDto categoryDto);
  int deleteCategoryById(long categoryId);
}

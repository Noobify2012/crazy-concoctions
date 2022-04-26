package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo {
  List<Category> getAllCategories();
  Optional<Category> getCategoryById(long id);
}

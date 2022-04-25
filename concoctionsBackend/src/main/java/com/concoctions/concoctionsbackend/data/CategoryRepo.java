package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Category;

import java.util.List;

public interface CategoryRepo {
  List<Category> getAllCategories();
  Category getCategoryById(long id);
}

package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.CategoryRepo;
import com.concoctions.concoctionsbackend.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCategoryRepo implements CategoryRepo {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcCategoryRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }


  @Override
  public List<Category> getAllCategories() {
    return jdbcTemplate.query(
        "select * from category",
        this::mapRowToCategory
    );
  }

  @Override
  public Optional<Category> getCategoryById(long id) {
    return jdbcTemplate.query(
        "select * from category where categoryId = ?",
        this::mapRowToCategory,
        id).stream()
        .findFirst();
  }

  private Category mapRowToCategory(ResultSet row, int rowNum)
      throws SQLException
  {
    return new Category(
        row.getLong("categoryId"),
        row.getString("name"),
        row.getString("description")
    );
  }
}

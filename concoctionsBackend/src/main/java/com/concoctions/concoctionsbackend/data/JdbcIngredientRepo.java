package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JdbcIngredientRepo implements IngredientRepo {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcIngredientRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }


  @Override
  public List<Ingredient> getAllIngredients() {
    return null;
  }

  private Ingredient mapRowToIngredient(ResultSet row, int rowNum)
      throws SQLException
  {
    return Ingredient.builder().build();
  }
}


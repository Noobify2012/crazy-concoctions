package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.DrinkIngredientRepo;
import com.concoctions.concoctionsbackend.data.IngredientRepo;
import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.model.DrinkIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDrinkIngredientRepo implements DrinkIngredientRepo {

  private final JdbcTemplate jdbcTemplate;
  private final UomRepo uomRepo;
  private final IngredientRepo ingredientRepo;

  @Autowired
  public JdbcDrinkIngredientRepo(
      JdbcTemplate jdbcTemplate,
      UomRepo uomRepo,
      IngredientRepo ingredientRepo)
  {
    this.jdbcTemplate = jdbcTemplate;
    this.uomRepo = uomRepo;
    this.ingredientRepo = ingredientRepo;
  }

  @Override
  public List<DrinkIngredient> getAllDrinkIngredientForDrinkId(long id) {
    return jdbcTemplate.query(
        "select * from drink_ingredient where drinkId = ?",
        this::mapRowToDrinkIngredient,
        id);
  }

  private DrinkIngredient mapRowToDrinkIngredient(ResultSet row, int rowNum)
      throws SQLException
  {
    return new DrinkIngredient(
        // todo make sure to actually throw an error here and not just pass null
        uomRepo.getUomById(row.getLong("uomId"))
            .orElse(null),
        ingredientRepo.getIngredientById(row.getLong("ingredientId"))
            .orElse(null),
        row.getDouble("amount")
    );
  }
}

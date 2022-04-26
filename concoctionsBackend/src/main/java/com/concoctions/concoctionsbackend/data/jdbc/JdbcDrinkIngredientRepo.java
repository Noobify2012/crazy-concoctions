package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.DrinkIngredientRepo;
import com.concoctions.concoctionsbackend.data.IngredientRepo;
import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.dto.DrinkIngredientDto;
import com.concoctions.concoctionsbackend.model.DrinkIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDrinkIngredientRepo implements DrinkIngredientRepo {

  private final JdbcTemplate jdbcTemplate;
  private final UomRepo uomRepo;
  private final IngredientRepo ingredientRepo;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcDrinkIngredientRepo(
      JdbcTemplate jdbcTemplate,
      UomRepo uomRepo,
      IngredientRepo ingredientRepo,
      DataSource dataSource
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.uomRepo = uomRepo;
    this.ingredientRepo = ingredientRepo;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("drink_ingredient")
        .usingColumns("drinkId", "ingredientId", "uomId", "amount");
  }

  @Override
  public List<DrinkIngredient> getAllForDrinkId(long id) {
    return jdbcTemplate.query(
        "select * from drink_ingredient where drinkId = ?",
        this::mapRowToDrinkIngredient,
        id);
  }

  @Override
  public List<DrinkIngredient> saveAll(
      List<DrinkIngredientDto> drinkIngredientDtos) {

    return null;
  }

  private DrinkIngredient mapRowToDrinkIngredient(ResultSet row, int rowNum)
      throws SQLException
  {
    return new DrinkIngredient(
        // todo make sure to actually throw an error here and not just pass null
        uomRepo.getUomById(row.getLong("uomId"))
            .orElse(null),
        ingredientRepo.getById(row.getLong("ingredientId"))
            .orElse(null),
        row.getDouble("amount")
    );
  }
}

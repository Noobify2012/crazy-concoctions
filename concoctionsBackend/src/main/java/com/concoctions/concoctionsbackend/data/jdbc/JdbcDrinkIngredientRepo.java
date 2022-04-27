package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.DrinkIngredientRepo;
import com.concoctions.concoctionsbackend.data.IngredientRepo;
import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.dto.DrinkIngredientDto;
import com.concoctions.concoctionsbackend.model.DrinkIngredient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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
        .withTableName("drink_ingredient");
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
      long drinkId,
      List<DrinkIngredientDto> drinkIngredientDtos) {

    // https://www.tabnine.com/code/java/methods/org.springframework.jdbc.core.simple.SimpleJdbcInsert/executeBatch
    MapSqlParameterSource[] paramsList = drinkIngredientDtos.stream()
        .map(drinkIngredientDto -> new MapSqlParameterSource()
            .addValue("drinkId", drinkId)
            .addValue("ingredientId", drinkIngredientDto.getIngredientId())
            .addValue("uomId", drinkIngredientDto.getUomId())
            .addValue("amount", drinkIngredientDto.getAmount())
        ).toArray(MapSqlParameterSource[]::new);

    simpleJdbcInsert.executeBatch(paramsList);

    return this.getAllForDrinkId(drinkId);
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

package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.DrinkIngredientRepo;
import com.concoctions.concoctionsbackend.data.IngredientRepo;
import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.dto.DrinkIngredientDto;
import com.concoctions.concoctionsbackend.model.DrinkIngredient;
import com.fasterxml.jackson.databind.util.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Repository
public class JdbcDrinkIngredientRepo implements DrinkIngredientRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final UomRepo uomRepo;
  private final IngredientRepo ingredientRepo;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcDrinkIngredientRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      UomRepo uomRepo,
      IngredientRepo ingredientRepo,
      DataSource dataSource
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
        .map(dto -> new MapSqlParameterSource()
            .addValue("drinkId", drinkId)
            .addValue("ingredientId", dto.getIngredientId())
            .addValue("uomId", dto.getUomId())
            .addValue("amount", dto.getAmount())
        ).toArray(MapSqlParameterSource[]::new);

    simpleJdbcInsert.executeBatch(paramsList);

    return this.getAllForDrinkId(drinkId);
  }

  @Override
  public int deleteAll(
      long drinkId,
      List<DrinkIngredientDto> drinkIngredientDtos
  ){
    MapSqlParameterSource[] paramsList = drinkIngredientDtos.stream()
        .map(dto -> new MapSqlParameterSource()
            .addValue("drinkId", drinkId)
            .addValue("ingredientId", dto.getIngredientId())
            .addValue("uomId", dto.getUomId())
        ).toArray(MapSqlParameterSource[]::new);

    String update = "delete from drink_ingredient where drinkId = :drinkId and ingredientId = :ingredientId and uomId = :uomId";
    int[] numChanged = namedParameterJdbcTemplate.batchUpdate(update, paramsList);
    return Arrays.stream(numChanged).sum();
  }

  private DrinkIngredient mapRowToDrinkIngredient(ResultSet row, int rowNum)
      throws SQLException
  {
    return new DrinkIngredient(
        // todo make sure to actually throw an error here and not just pass null
        uomRepo.getById(row.getLong("uomId"))
            .orElse(null),
        ingredientRepo.getById(row.getLong("ingredientId"))
            .orElse(null),
        row.getDouble("amount")
    );
  }
}

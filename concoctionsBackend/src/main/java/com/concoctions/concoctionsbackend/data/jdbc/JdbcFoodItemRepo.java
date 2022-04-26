package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.FoodItemRepo;
import com.concoctions.concoctionsbackend.dto.FoodItemDto;
import com.concoctions.concoctionsbackend.model.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFoodItemRepo implements FoodItemRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcFoodItemRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      DataSource dataSource
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("foodItem")
        .usingGeneratedKeyColumns("foodItemId");
  }

  @Override
  public List<FoodItem> getAll() {
    return jdbcTemplate.query(
        "select * from foodItem",
        this::mapRowToFoodItems
    );
  }

  @Override
  public List<FoodItem> getAllByDrinkId(long foodItemId) {
    List<Long> foodItemIds = jdbcTemplate.query(
        "select foodItemId from pairing where drinkId = ?",
        (row, rowNum) -> row.getLong("foodItemId"),
        foodItemId);

    // https://www.baeldung.com/spring-jdbctemplate-in-list
    SqlParameterSource parameters
        = new MapSqlParameterSource("foodItemIds", foodItemIds);

    return namedParameterJdbcTemplate.query(
        "select * from foodItem where foodItemId in (:foodItemIds)",
        parameters,
        this::mapRowToFoodItems
    );
  }

  @Override
  public Optional<FoodItem> getById(long foodItemId) {
    return jdbcTemplate.query(
        "select * from foodItem where foodItemId = ?",
        this::mapRowToFoodItems,
            foodItemId).stream()
        .findFirst();
  }

  @Override
  public Optional<FoodItem> save(FoodItemDto foodItemDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("name", foodItemDto.getName());

    Number key = simpleJdbcInsert.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();

  }

  @Override
  public int deleteFoodItemById(long foodItemId) {
    return jdbcTemplate.update(
        "delete from foodItem where foodItemId = ?",
        foodItemId);
  }

  private FoodItem mapRowToFoodItems(ResultSet row, int rowNum)
      throws SQLException
  {
    return new FoodItem(
        row.getLong("foodItemId"),
        row.getString("name")
    );
  }
}

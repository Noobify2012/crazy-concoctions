package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.FoodItemRepo;
import com.concoctions.concoctionsbackend.model.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFoodItemRepo implements FoodItemRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public JdbcFoodItemRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate

  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public List<FoodItem> getAllFoodItems() {
    return jdbcTemplate.query(
        "select * from foodItem",
        this::mapRowToFoodItems
    );
  }

  @Override
  public List<FoodItem> getAllFoodItemsByDrinkId(long id) {
    List<Long> foodItemIds = jdbcTemplate.query(
        "select foodItemId from pairing where drinkId = ?",
        (row, rowNum) -> row.getLong("foodItemId"),
        id);

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
  public Optional<FoodItem> getFoodItemById(long id) {
    return jdbcTemplate.query(
        "select * from foodItem where foodItemId = ?",
        this::mapRowToFoodItems,
        id).stream()
        .findFirst();
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

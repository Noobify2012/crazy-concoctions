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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcFoodItemRepo implements FoodItemRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsertFoodItem;
  private final SimpleJdbcInsert simpleJdbcInsertPairing;

  @Autowired
  public JdbcFoodItemRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      DataSource dataSource
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.simpleJdbcInsertFoodItem = new SimpleJdbcInsert(dataSource)
        .withTableName("foodItem")
        .usingGeneratedKeyColumns("foodItemId");
    this.simpleJdbcInsertPairing = new SimpleJdbcInsert(dataSource)
        .withTableName("pairing");
  }

  @Override
  public List<FoodItem> getAll() {
    return jdbcTemplate.query(
        "select * from foodItem",
        this::mapRowToFoodItems
    );
  }

  @Override
  public List<FoodItem> getAllByDrinkId(long drinkId) {
    return jdbcTemplate.query(
        "select foodItem.* from foodItem JOIN pairing using (foodItemId) "
        + " where drinkId = ?",
        this::mapRowToFoodItems,
        drinkId);
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

    Number key = simpleJdbcInsertFoodItem.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();
  }

  @Override
  public Optional<FoodItem> update(long foodItemId, FoodItemDto foodItemDto) {
    String update = "update foodItem set name = :name";
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("name", foodItemDto.getName());

    int numChanged = namedParameterJdbcTemplate.update(update, params);
    if (numChanged > 0) {
      return this.getById(foodItemId);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public List<FoodItem> saveAllByDrinkId(long drinkId, List<Long> foodItemIds) {
    MapSqlParameterSource[] paramsList = foodItemIds.stream()
        .map(foodItemId -> new MapSqlParameterSource()
            .addValue("drinkId", drinkId)
            .addValue("foodItemID", foodItemId)
        ).toArray(MapSqlParameterSource[]::new);
    simpleJdbcInsertPairing.executeBatch(paramsList);
    return this.getAllByDrinkId(drinkId);
  }

  @Override
  public int deleteAllByDrinkId(long drinkId, List<Long> foodItemIds) {
    MapSqlParameterSource[] paramsList = foodItemIds.stream()
        .map(l -> new MapSqlParameterSource()
            .addValue("foodItemId", l)
            .addValue("drinkId", drinkId)
        ).toArray(MapSqlParameterSource[]::new);
    String update = "delete from pairing where drinkId = :drinkId and foodItemid = :foodItemId";
    int[] numChanged = namedParameterJdbcTemplate.batchUpdate(update, paramsList);
    return Arrays.stream(numChanged).sum();
  }

  @Override
  public int deleteById(long foodItemId) {
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

package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.CategoryRepo;
import com.concoctions.concoctionsbackend.data.DrinkIngredientRepo;
import com.concoctions.concoctionsbackend.data.DrinkRepo;
import com.concoctions.concoctionsbackend.data.FoodItemRepo;
import com.concoctions.concoctionsbackend.dto.DrinkDto;
import com.concoctions.concoctionsbackend.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcDrinkRepo implements DrinkRepo {

  private final JdbcTemplate jdbcTemplate;
  private final DrinkIngredientRepo drinkIngredientRepo;
  private final CategoryRepo categoryRepo;
  private final FoodItemRepo foodItemRepo;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcDrinkRepo(
      JdbcTemplate jdbcTemplate,
      DrinkIngredientRepo drinkIngredientRepo,
      CategoryRepo categoryRepo,
      FoodItemRepo foodItemRepo,
      DataSource dataSource
  ){
    this.jdbcTemplate = jdbcTemplate;
    this.drinkIngredientRepo = drinkIngredientRepo;
    this.categoryRepo = categoryRepo;
    this.foodItemRepo = foodItemRepo;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("drink")
        .usingGeneratedKeyColumns("drinkId");
  }

  @Override
  public List<Drink> getAll() {
    return jdbcTemplate.query(
        "select * from drink",
        this::mapRowToDrink
    );
  }

  @Override
  public Optional<Drink> findById(long drinkId) {
    return jdbcTemplate.query(
        "select * from drink where drinkId = ?",
        this::mapRowToDrink,
        drinkId).stream()
        .findFirst();
  }

  @Override
  public Optional<Drink> save(DrinkDto drinkDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("userId", drinkDto.getUserId())
        .addValue("name" , drinkDto.getName())
        .addValue("categoryId", drinkDto.getCategoryId())
        .addValue("isHot", drinkDto.isHot())
        .addValue("description", drinkDto.getDescription());
    Number key = simpleJdbcInsert.executeAndReturnKey(params);
    return null;
  }

  @Override
  public int deleteDrinkById(long drinkId) {
    return jdbcTemplate.update(
        "delete from drink where drinkId = ?",
        drinkId);
  }

  private Drink mapRowToDrink(ResultSet row, int rowNum) throws SQLException {
    return Drink.builder()
        .drinkId(row.getLong("drinkId"))
        .userId(row.getLong("userId"))
        .name(row.getString("name"))
        .category(
            categoryRepo
                .getById(row.getLong("categoryId"))
                .orElse(null)
            // todo really need a proper error check here and not just return null
        )
        .isHot(row.getBoolean("isHot"))
        .description(row.getString("description"))
        .drinkIngredients(
            drinkIngredientRepo
                .getAllForDrinkId(row.getLong("drinkId")))
        .pairings(
            foodItemRepo
                .getAllByDrinkId(row.getLong("drinkId")))
        .build();
  }
}

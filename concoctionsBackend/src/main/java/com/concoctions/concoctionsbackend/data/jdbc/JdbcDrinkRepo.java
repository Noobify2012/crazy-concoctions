package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.CategoryRepo;
import com.concoctions.concoctionsbackend.data.DrinkIngredientRepo;
import com.concoctions.concoctionsbackend.data.DrinkRepo;
import com.concoctions.concoctionsbackend.data.FoodItemRepo;
import com.concoctions.concoctionsbackend.dto.DrinkDto;
import com.concoctions.concoctionsbackend.dto.DrinkIngredientDto;
import com.concoctions.concoctionsbackend.model.Drink;
import com.concoctions.concoctionsbackend.model.DrinkIngredient;
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
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Repository
public class JdbcDrinkRepo implements DrinkRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final DrinkIngredientRepo drinkIngredientRepo;
  private final CategoryRepo categoryRepo;
  private final FoodItemRepo foodItemRepo;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcDrinkRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      DrinkIngredientRepo drinkIngredientRepo,
      CategoryRepo categoryRepo,
      FoodItemRepo foodItemRepo,
      DataSource dataSource
  ){
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
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
  public Optional<Drink> getById(long drinkId) {
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

    List<DrinkIngredient> drinkIngredients =
        drinkIngredientRepo.saveAll(
            key.longValue(),
            drinkDto.getDrinkIngredients()
        );
    List<FoodItem> foodItems =
        foodItemRepo.saveAllByDrinkId(
            key.longValue(),
            drinkDto.getFoodItemIds()
        );
    return this.getById(key.longValue()).stream().findFirst();
  }

  @Override
  public Optional<Drink> update(long drinkId, DrinkDto drinkDto) {
    String update = "update drink set userId = :userId, categoryId = :categoryId, name = :name, "
        + "isHot = :isHot, description = :description where drinkId = :drinkId";
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("drinkId", drinkId)
        .addValue("userId", drinkDto.getUserId())
        .addValue("categoryId", drinkDto.getCategoryId())
        .addValue("name", drinkDto.getName())
        .addValue("isHot", drinkDto.isHot())
        .addValue("description", drinkDto.getDescription());

    int numChanged = namedParameterJdbcTemplate.update(update, params);

    // change up drink_ingredient
    List<DrinkIngredientDto> oldDrinkIngredients = jdbcTemplate.query(
        "select * from drink_ingredient where drinkId = ?",
        this::mapRowToDrinkIngredient,
        drinkId);

    List<DrinkIngredientDto> drinkIngredientToRemoveList = oldDrinkIngredients.stream()
        .filter(di -> !drinkDto.getDrinkIngredients().contains(di))
        .collect(Collectors.toList());
    List<DrinkIngredientDto> drinkIngredientToAddList = drinkDto.getDrinkIngredients().stream()
        .filter(di -> !oldDrinkIngredients.contains(di))
        .collect(Collectors.toList());

    int drinkIngredientsDeleted = drinkIngredientRepo.deleteAll(drinkId, drinkIngredientToRemoveList);
    List<DrinkIngredient> addedIngredients =
        drinkIngredientRepo.saveAll(drinkId, drinkIngredientToAddList);


    List<Long> oldFoodItemIds = foodItemRepo.getAllByDrinkId(drinkId).stream()
        .map(FoodItem::getFoodItemId).collect(Collectors.toList());

    List<Long> foodItemsToRemoveList = oldFoodItemIds.stream()
        .filter(foodItemId -> !drinkDto.getFoodItemIds().contains(foodItemId))
        .collect(Collectors.toList());
    List<Long> foodItemsToAddList = drinkDto.getFoodItemIds().stream()
        .filter(foodItemId -> !oldFoodItemIds.contains(foodItemId))
        .collect(Collectors.toList());

    int foodRowsDeleted = foodItemRepo.deleteAllByDrinkId(drinkId, foodItemsToRemoveList);
    List<FoodItem> addedFoodItems = foodItemRepo.saveAllByDrinkId(drinkId, foodItemsToAddList);

    return this.getById(drinkId);

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

  private DrinkIngredientDto mapRowToDrinkIngredient(ResultSet row, int rowNum)
      throws SQLException
  {
    return new DrinkIngredientDto(
        row.getLong("ingredientId"),
        row.getLong("uomId"),
        row.getDouble("amount"));
  }
}

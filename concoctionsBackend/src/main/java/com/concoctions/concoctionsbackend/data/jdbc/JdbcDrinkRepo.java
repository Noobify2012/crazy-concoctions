package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.CategoryRepo;
import com.concoctions.concoctionsbackend.data.DrinkIngredientRepo;
import com.concoctions.concoctionsbackend.data.DrinkRepo;
import com.concoctions.concoctionsbackend.data.FoodItemRepo;
import com.concoctions.concoctionsbackend.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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

  @Autowired
  public JdbcDrinkRepo(
      JdbcTemplate jdbcTemplate,
      DrinkIngredientRepo drinkIngredientRepo,
      CategoryRepo categoryRepo,
      FoodItemRepo foodItemRepo
  ){
    this.jdbcTemplate = jdbcTemplate;
    this.drinkIngredientRepo = drinkIngredientRepo;
    this.categoryRepo = categoryRepo;
    this.foodItemRepo = foodItemRepo;
  }

  @Override
  public List<Drink> getAllDrinks() {
    return jdbcTemplate.query(
        "select * from drink",
        this::mapRowToDrink
    );
  }

  @Override
  public Optional<Drink> findDrinkById(Long id) {
    return jdbcTemplate.query(
        "select * from drink where drinkId = ?",
        this::mapRowToDrink,
        id).stream()
        .findFirst();
  }


  private Drink mapRowToDrink(ResultSet row, int rowNum) throws SQLException {
    return Drink.builder()
        .drinkId(row.getLong("drinkId"))
        .userId(row.getLong("userId"))
        .name(row.getString("name"))
        .category(
            categoryRepo
                .getCategoryById(row.getLong("categoryId"))
                .orElse(null)
            // todo really need a proper error check here and not just return null
        )
        .isHot(row.getBoolean("isHot"))
        .description(row.getString("description"))
        .drinkIngredients(
            drinkIngredientRepo
                .getAllDrinkIngredientForDrinkId(row.getLong("drinkId")))
        .pairings(
            foodItemRepo
                .getAllFoodItemsByDrinkId(row.getLong("drinkId")))
        .build();
  }
}

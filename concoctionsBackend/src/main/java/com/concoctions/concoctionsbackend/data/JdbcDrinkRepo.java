package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Drink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDrinkRepo implements DrinkRepo {

  private final JdbcTemplate jdbcTemplate;
  private final DrinkIngredientRepo drinkIngredientRepo;
  private final CategoryRepo categoryRepo;

  @Autowired
  public JdbcDrinkRepo(
      JdbcTemplate jdbcTemplate,
      DrinkIngredientRepo drinkIngredientRepo,
      CategoryRepo categoryRepo
  ){
    this.jdbcTemplate = jdbcTemplate;
    this.drinkIngredientRepo = drinkIngredientRepo;
    this.categoryRepo = categoryRepo;
  }

  @Override
  public List<Drink> getAllDrinks() {
    return jdbcTemplate.query(
        "select * from drink",
        this::mapRowToDrink
    );
  }

  @Override
  public Drink findDrinkById(Long id) {
    return jdbcTemplate.query(
        "select * from drink where drinkId = ?",
        this::mapRowToDrink,
        id).stream()
        .findFirst()
        .orElse(null);
    // todo make sure to actually throw an error here.
  }


  private Drink mapRowToDrink(ResultSet row, int rowNum) throws SQLException {
    return Drink.builder()
        .drinkId(row.getLong("drinkId"))
        .userId(row.getLong("userId"))
        .name(row.getString("name"))
        .category(categoryRepo.getCategoryById(row.getLong("categoryId")))
        .isHot(row.getBoolean("isHot"))
        .description(row.getString("description"))
        .drinkIngredients(drinkIngredientRepo.getAllDrinkIngredientForDrinkId(row.getLong("drinkId")))
//        .pairings(null)
        // todo have to add pairings
        .build();
  }
}

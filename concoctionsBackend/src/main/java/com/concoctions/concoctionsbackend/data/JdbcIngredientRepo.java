package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Ingredient;
import com.concoctions.concoctionsbackend.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcIngredientRepo implements IngredientRepo {

  private final JdbcTemplate jdbcTemplate;
  private final TypeRepo typeRepo;

  @Autowired
  public JdbcIngredientRepo(JdbcTemplate jdbcTemplate, TypeRepo typeRepo) {
    this.jdbcTemplate = jdbcTemplate;
    this.typeRepo = typeRepo;
  }


  @Override
  public List<Ingredient> getAllIngredients() {
    List<Type> types = typeRepo.getAllTypes();
    return jdbcTemplate.query(
        "select * from ingredient",
        (row, rowNum) -> {
          long typeID = row.getLong("typeID");
          Type type = types.stream()
              .filter(t -> t.getTypID() == typeID)
              .findFirst()
              .orElse(null);
          // todo make sure to actually throw an error here.
          return Ingredient.builder()
              .ingredientId(row.getLong("ingredientId"))
              .name(row.getString("name"))
              .description(row.getString("description"))
              .type(type)
              .build();
        }
    );
  }

  @Override
  public Ingredient getIngredientById(long id) {
    return jdbcTemplate.query(
        "select * from ingredient where ingredientId = ?",
        this::mapRowToIngredient,
        id).stream()
        .findFirst()
        .orElse(null);
    // todo make sure to actually throw an error here.
  }

  private Ingredient mapRowToIngredient(ResultSet row, int rowNum)
      throws SQLException
  {
    return Ingredient.builder()
        .ingredientId(row.getLong("ingredientId"))
        .name(row.getString("name"))
        .description(row.getString("description"))
        .type(typeRepo.getTypeById(row.getLong("typeId")))
        .build();
  }

}


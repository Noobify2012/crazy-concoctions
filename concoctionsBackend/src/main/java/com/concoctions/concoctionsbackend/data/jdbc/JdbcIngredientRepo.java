package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.IngredientRepo;
import com.concoctions.concoctionsbackend.data.TypeRepo;
import com.concoctions.concoctionsbackend.dto.Ingredient;
import com.concoctions.concoctionsbackend.dto.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
              .filter(t -> t.getTypeID() == typeID)
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
  public Optional<Ingredient> getIngredientById(long ingredientId) {
    return jdbcTemplate.query(
        "select * from ingredient where ingredientId = ?",
        this::mapRowToIngredient,
            ingredientId).stream()
        .findFirst();
  }

  @Override
  public int deleteIngredientById(long ingredientId) {
    return jdbcTemplate.update(
        "delete from ingredient where ingredientId = ?",
        ingredientId);
  }

  private Ingredient mapRowToIngredient(ResultSet row, int rowNum)
      throws SQLException
  {
    return Ingredient.builder()
        .ingredientId(row.getLong("ingredientId"))
        .name(row.getString("name"))
        .description(row.getString("description"))
        .type(typeRepo.getTypeById(row.getLong("typeId")).orElse(null))
        // todo make sure to actually throw an error here.
        .build();
  }

}


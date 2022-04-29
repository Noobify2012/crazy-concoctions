package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.IngredientRepo;
import com.concoctions.concoctionsbackend.data.TypeRepo;
import com.concoctions.concoctionsbackend.dto.IngredientDto;
import com.concoctions.concoctionsbackend.model.Ingredient;
import com.concoctions.concoctionsbackend.model.Type;
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
public class JdbcIngredientRepo implements IngredientRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final TypeRepo typeRepo;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcIngredientRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      TypeRepo typeRepo,
      DataSource dataSource
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.typeRepo = typeRepo;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("ingredient")
        .usingGeneratedKeyColumns("ingredientId");
  }


  @Override
  public List<Ingredient> getAll() {
    List<Type> types = typeRepo.getAll();
    return jdbcTemplate.query(
        "select * from ingredient",
        (row, rowNum) -> {
          long typeID = row.getLong("typeID");
          Type type = types.stream()
              .filter(t -> t.getTypeId() == typeID)
              .findFirst()
              .orElse(null);
          // todo make sure to actually throw an error here.
          return Ingredient.builder()
              .ingredientId(row.getLong("ingredientId"))
              .name(row.getString("name"))
              .description(row.getString("description"))
              .type(type)
              .isAlcoholic(row.getBoolean("isAlcoholic"))
              .build();
        }
    );
  }

  @Override
  public Optional<Ingredient> getById(long ingredientId) {
    return jdbcTemplate.query(
        "select * from ingredient where ingredientId = ?",
        this::mapRowToIngredient,
            ingredientId).stream()
        .findFirst();
  }

  @Override
  public Optional<Ingredient> save(IngredientDto ingredientDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("name", ingredientDto.getName())
        .addValue("typeId", ingredientDto.getTypeId())
        .addValue("description", ingredientDto.getDescription())
        .addValue("isAlcoholic", ingredientDto.isAlcoholic());

    Number key = simpleJdbcInsert.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();
  }

  @Override
  public Optional<Ingredient> update(long ingredientId, IngredientDto ingredientDto) {
    String update = "update ingredient set name = :name, typeId = :typeId, "
        + "description = :description, isAlcoholic = :isAlcoholic "
        + "where ingredientId = :ingredientId";
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("ingredientId", ingredientId)
        .addValue("name", ingredientDto.getName())
        .addValue("typeId", ingredientDto.getTypeId())
        .addValue("description", ingredientDto.getDescription())
        .addValue("isAlcoholic", ingredientDto.isAlcoholic());


    int numChanged = namedParameterJdbcTemplate.update(update, params);
    if (numChanged > 0) {
      return this.getById(ingredientId);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public int deleteById(long ingredientId) {
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
        .type(typeRepo.getById(row.getLong("typeId")).orElse(null))
        // todo make sure to actually throw an error here.
        .isAlcoholic(row.getBoolean("isAlcoholic"))
        .build();
  }

}


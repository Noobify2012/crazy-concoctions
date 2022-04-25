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

  @Autowired
  public JdbcDrinkRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }


  @Override
  public List<Drink> getAllDrinks() {
    return null;
  }

  @Override
  public Drink findDrinkById(Long id) {
    return null;
  }


  private Drink mapRowToDrink(ResultSet row, int rowNum) throws SQLException {
    return Drink.builder()
        .name(row.getString("name"))
        .build();
  }
}

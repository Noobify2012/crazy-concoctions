package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.CategoryRepo;
import com.concoctions.concoctionsbackend.dto.CategoryDto;
import com.concoctions.concoctionsbackend.model.Category;
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
public class JdbcCategoryRepo implements CategoryRepo {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcCategoryRepo(JdbcTemplate jdbcTemplate, DataSource dataSource) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("category")
        .usingGeneratedKeyColumns("categoryId");
  }


  @Override
  public List<Category> getAll() {
    return jdbcTemplate.query(
        "select * from category",
        this::mapRowToCategory
    );
  }

  @Override
  public Optional<Category> getById(long categoryId) {
    return jdbcTemplate.query(
        "select * from category where categoryId = ?",
        this::mapRowToCategory,
            categoryId).stream()
        .findFirst();
  }

  @Override
  public Optional<Category> save(CategoryDto categoryDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("name", categoryDto.getName())
        .addValue("description", categoryDto.getDescription());

    Number key = simpleJdbcInsert.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();
  }

  @Override
  public int deleteCategoryById(long categoryId) {
    return jdbcTemplate.update(
        "delete from category where categoryId = ?",
        categoryId);
  }

  private Category mapRowToCategory(ResultSet row, int rowNum)
      throws SQLException
  {
    return new Category(
        row.getLong("categoryId"),
        row.getString("name"),
        row.getString("description")
    );
  }
}

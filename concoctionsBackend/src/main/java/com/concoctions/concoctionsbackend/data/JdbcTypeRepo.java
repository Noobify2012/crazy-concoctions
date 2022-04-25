package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcTypeRepo implements TypeRepo {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcTypeRepo(JdbcTemplate jdbcTemplate){
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Type> getAllTypes() {
    return jdbcTemplate.query(
        "select * from type",
        this::mapRowToType
    );
  }

  @Override
  public Type getTypeById(long id) {
    return jdbcTemplate.query(
        "select * from type where typeId = ?",
        this::mapRowToType,
        id).stream()
        .findFirst()
        .orElse(null);
    // todo make sure to actually throw an error here.

  }

  @Override
  public Type getTypeByName(String name) {
    return jdbcTemplate.query(
        "select * from type where name like ?",
        this::mapRowToType,
        name).stream()
        .findFirst()
        .orElse(null);
    // todo make sure to actually throw an error here.
  }

  private Type mapRowToType(ResultSet row, int rowNum) throws SQLException {
    return new Type(
        row.getLong("typeID"),
        row.getString("name"),
        row.getString("description")
    );
  }
}

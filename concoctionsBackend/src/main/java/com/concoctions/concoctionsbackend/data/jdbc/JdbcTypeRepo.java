package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.TypeRepo;
import com.concoctions.concoctionsbackend.dto.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
  public Optional<Type> getTypeById(long typeId) {
    return jdbcTemplate.query(
        "select * from type where typeId = ?",
        this::mapRowToType,
            typeId).stream()
        .findFirst();

  }

  @Override
  public Optional<Type> getTypeByName(String name) {
    return jdbcTemplate.query(
        "select * from type where name like ?",
        this::mapRowToType,
        name).stream()
        .findFirst();
  }

  @Override
  public int deleteById(long typeId) {
    return jdbcTemplate.update(
        "delete from type where typeId = ?",
        typeId
    );

  }

  private Type mapRowToType(ResultSet row, int rowNum) throws SQLException {
    return new Type(
        row.getLong("typeID"),
        row.getString("name"),
        row.getString("description")
    );
  }
}

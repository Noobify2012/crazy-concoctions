package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.TypeRepo;
import com.concoctions.concoctionsbackend.dto.TypeDto;
import com.concoctions.concoctionsbackend.model.Type;
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
public class JdbcTypeRepo implements TypeRepo {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcTypeRepo(JdbcTemplate jdbcTemplate, DataSource dataSource ){
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("type")
        .usingGeneratedKeyColumns("typeId");
  }

  @Override
  public List<Type> getAll() {
    return jdbcTemplate.query(
        "select * from type",
        this::mapRowToType
    );
  }

  @Override
  public Optional<Type> getById(long typeId) {
    return jdbcTemplate.query(
        "select * from type where typeId = ?",
        this::mapRowToType,
            typeId).stream()
        .findFirst();

  }

  @Override
  public Optional<Type> getByName(String name) {
    return jdbcTemplate.query(
        "select * from type where name like ?",
        this::mapRowToType,
        name).stream()
        .findFirst();
  }

  @Override
  public Optional<Type> save(TypeDto typeDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("name", typeDto.getName())
        .addValue("description", typeDto.getDescription());

    Number key = simpleJdbcInsert.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();
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

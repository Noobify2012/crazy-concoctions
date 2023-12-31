package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.dto.UomDto;
import com.concoctions.concoctionsbackend.model.UnitOfMeasure;
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
public class JdbcUomRepo implements UomRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SimpleJdbcInsert simpleJDbcInsert;

  @Autowired
  public JdbcUomRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      DataSource dataSource
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.simpleJDbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("unitOfMeasure")
        .usingGeneratedKeyColumns("uomId");
  }


  @Override
  public List<UnitOfMeasure> getAll() {
    return jdbcTemplate.query(
        "select * from unitOfMeasure",
        this::mapRowToUom
    );
  }

  @Override
  public Optional<UnitOfMeasure> getById(long uomId) {
    return jdbcTemplate.query(
        "select * from unitOfMeasure where uomId = ?",
        this::mapRowToUom,
            uomId).stream()
        .findFirst();
  }

  @Override
  public Optional<UnitOfMeasure> getByName(String name) {
    return jdbcTemplate.query(
        "select * from unitOfMeasure where name = ?",
        this::mapRowToUom,
        name).stream()
        .findFirst();
  }

  @Override
  public Optional<UnitOfMeasure> update(long uomId, UomDto uomDto) {
    String update = "update unitOfMeasure set name = :name, type = :type where "
        + "uomId = :uomId";
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("uomId", uomId)
        .addValue("name", uomDto.getName())
        .addValue("type", uomDto.getType());
    int numChanged = namedParameterJdbcTemplate.update(update, params);
    if (numChanged > 0) {
      return this.getById(uomId);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<UnitOfMeasure> save(UomDto uomDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("name", uomDto.getName())
        .addValue("type", uomDto.getType());

    Number key = simpleJDbcInsert.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();
  }

  @Override
  public int deleteById(long uomId) {
    return jdbcTemplate.update(
        "delete from unitOfMeasure where uomId = ?",
        uomId);

  }

  private UnitOfMeasure mapRowToUom(ResultSet row, int rowNum)
     throws SQLException
  {
    return new UnitOfMeasure(
        row.getLong("uomId"),
        row.getString("name"),
        row.getString("type")
    );
  }
}

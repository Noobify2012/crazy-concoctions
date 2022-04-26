package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.UomRepo;
import com.concoctions.concoctionsbackend.model.UnitOfMeasure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcUomRepo implements UomRepo {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcUomRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }


  @Override
  public List<UnitOfMeasure> getAllUoms() {
    return jdbcTemplate.query(
        "select * from unitOfMeasure",
        this::mapRowToUom
    );
  }

  @Override
  public UnitOfMeasure getUomById(long id) {
    return jdbcTemplate.query(
        "select * from unitOfMeasure where uomId = ?",
        this::mapRowToUom,
        id).stream()
        .findFirst()
        .orElse(null);
    // todo make sure to actually throw an error here.
  }

  @Override
  public UnitOfMeasure getUomByName(String name) {
    return jdbcTemplate.query(
        "select * from unitOfMeasure where name = ?",
        this::mapRowToUom,
        name).stream()
        .findFirst()
        .orElse(null);
    // todo make sure to actually throw an error here.
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

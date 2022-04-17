package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcUserRepo implements UserRepo {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcUserRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<User> getAllUsers() {
    return jdbcTemplate.query(
        "select * from user",
        this::mapRowToUser);
  }

  private User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
    return User.builder()
        .userId(row.getLong("userId"))
        .email(row.getString("email"))
        .username(row.getString("username"))
        .password(row.getString("password"))
        .firstName(row.getString("firstName"))
        .lastName(row.getString("lastName"))
        .bio(row.getString("bio"))
        .build();
  }
}

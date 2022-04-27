package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.UserRepo;
import com.concoctions.concoctionsbackend.dto.UserDto;
import com.concoctions.concoctionsbackend.model.User;
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
public class JdbcUserRepo implements UserRepo {

  private final JdbcTemplate jdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public JdbcUserRepo(
      JdbcTemplate jdbcTemplate,
      DataSource dataSource,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("user")
        .usingGeneratedKeyColumns("userId");
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Optional<User> findUserByUsernameAndPassword(String username, String password) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("username", username)
        .addValue("password", password);
    return namedParameterJdbcTemplate.query(
        "select * from user where username = :username and password = :password",
        params,
        this::mapRowToUser
    ).stream().findFirst();
  }

  @Override
  public List<User> getAll() {
    return jdbcTemplate.query(
        "select * from user",
        this::mapRowToUser);
  }

  @Override
  public Optional<User> getById(long userId) {
    return jdbcTemplate.query(
        "select * from user where userId = ?",
        this::mapRowToUser,
            userId).stream()
        .findFirst();
  }

  @Override
  public Optional<User> getByEmail(String email) {
    return jdbcTemplate.query(
        "select * from user where email = ?",
        this::mapRowToUser,
        email).stream()
        .findFirst();
  }

  @Override
  public Optional<User> save(UserDto userDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("email", userDto.getEmail())
        .addValue("username", userDto.getUsername())
        .addValue("password", userDto.getPassword())
        .addValue("firstName", userDto.getFirstName())
        .addValue("lastName", userDto.getLastName())
        .addValue("bio", userDto.getBio());

    Number key = simpleJdbcInsert.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();
  }

  @Override
  public Optional<User> update(long userId, UserDto userDto) {
    String update = "update user set email = :email, username = :username, password = :password, "
        + "firstName = :firstName, lastName = :lastName, bio = :bio where userId = :userId";
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("userId", userId)
        .addValue("email", userDto.getEmail())
        .addValue("username", userDto.getUsername())
        .addValue("password", userDto.getPassword())
        .addValue("firstName", userDto.getFirstName())
        .addValue("lastName", userDto.getLastName())
        .addValue("bio", userDto.getBio());

    int numChanged = namedParameterJdbcTemplate.update(update, params);
    if (numChanged > 0) {
      return this.getById(userId);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public int deleteById(long id) {
    return jdbcTemplate.update(
        "delete from user where userId = ?",
        id
    );
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

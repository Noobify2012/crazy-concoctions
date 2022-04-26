package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.UserRepo;
import com.concoctions.concoctionsbackend.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepo implements UserRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public JdbcUserRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate
  ) {
    this.jdbcTemplate = jdbcTemplate;
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
  public List<User> getAllUsers() {
    return jdbcTemplate.query(
        "select * from user",
        this::mapRowToUser);
  }

  @Override
  public Optional<User> findUserById(long id) {
    return jdbcTemplate.query(
        "select * from user where userId = ?",
        this::mapRowToUser,
        id).stream()
        .findFirst();
  }

  @Override
  public Optional<User> findUserByEmail(String email) {
    return jdbcTemplate.query(
        "select * from user where email = ?",
        this::mapRowToUser,
        email).stream()
        .findFirst();
  }

  @Override
  public int save(User user) {
    // todo if we want to return the same user, but now with the ID, we'll
    //  have to the special jdbcTemplate thing that gives us the results of the
    //  insert
    return jdbcTemplate.update(
        "insert into user "
            + "(email, username, password, firstName, lastName, bio) values "
            + "(?, ?, ?, ?, ?, ?)",
        user.getEmail(), user.getUsername(), user.getPassword(),
        user.getFirstName(), user.getLastName(), user.getBio()
    );
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

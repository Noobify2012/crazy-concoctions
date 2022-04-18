package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.User;
import lombok.extern.slf4j.Slf4j;
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

  @Override
  public User findUserById(long id) {
    return jdbcTemplate.query(
        "select * from user where userId = ?",
        this::mapRowToUser,
        id).stream()
        .findAny()
        .orElse(null);
    // todo make sure to actually throw an error here
  }

  @Override
  public int save(User user) {
    return jdbcTemplate.update(
        "insert into user "
            + "(email, username, password, firstName, lastName, bio) values "
            + "(?, ?, ?, ?, ?, ?)",
        user.getEmail(), user.getUsername(), user.getPassword(),
        user.getFirstName(), user.getLastName(), user.getBio()
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

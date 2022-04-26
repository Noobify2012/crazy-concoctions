package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.CommentRepo;
import com.concoctions.concoctionsbackend.dto.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCommentRepo implements CommentRepo {

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcCommentRepo(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public List<Comment> getAllComments() {
    return jdbcTemplate.query(
        "select * from comment",
        this::mapRowToComment
    );
  }

  @Override
  public List<Comment> getCommentsByDrinkId(long drinkId) {
    return jdbcTemplate.query(
        "select * from comment where drinkId = ?",
        this::mapRowToComment,
        drinkId);
  }

  @Override
  public List<Comment> getCommentsByUserId(long userId) {
    return jdbcTemplate.query(
        "select * from comment where userId = ?",
        this::mapRowToComment,
        userId);
  }

  @Override
  public Optional<Comment> getCommentById(long commentId) {
    return jdbcTemplate.query(
        "select * from comment where commentId = ?",
        this::mapRowToComment,
        commentId).stream()
        .findFirst();
  }

  @Override
  public int deleteByID(long commentId) {
    return jdbcTemplate.update(
        "delete from comment where commentId = ?",
        commentId
    );
  }

  private Comment mapRowToComment(ResultSet row, int rowNum)
      throws SQLException
  {
    return Comment.builder()
        .commentId(row.getLong("commentId"))
        .userId(row.getLong("userID"))
        .drinkId(row.getLong("drinkId"))
        .ranking(row.getInt("ranking"))
        .commentBody(row.getString("commentBody"))
        .build();

  }
}

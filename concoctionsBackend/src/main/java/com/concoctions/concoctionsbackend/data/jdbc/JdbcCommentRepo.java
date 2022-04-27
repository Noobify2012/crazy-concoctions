package com.concoctions.concoctionsbackend.data.jdbc;

import com.concoctions.concoctionsbackend.data.CommentRepo;
import com.concoctions.concoctionsbackend.dto.CommentDto;
import com.concoctions.concoctionsbackend.model.Comment;
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
public class JdbcCommentRepo implements CommentRepo {

  private final JdbcTemplate jdbcTemplate;
  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  private final SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public JdbcCommentRepo(
      JdbcTemplate jdbcTemplate,
      NamedParameterJdbcTemplate namedParameterJdbcTemplate,
      DataSource dataSource
  ) {
    this.jdbcTemplate = jdbcTemplate;
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
        .withTableName("comment")
        .usingGeneratedKeyColumns("commentId");
  }

  @Override
  public List<Comment> getAll() {
    return jdbcTemplate.query(
        "select * from comment",
        this::mapRowToComment
    );
  }

  @Override
  public List<Comment> getAllByDrinkId(long drinkId) {
    return jdbcTemplate.query(
        "select * from comment where drinkId = ?",
        this::mapRowToComment,
        drinkId);
  }

  @Override
  public List<Comment> getAllByUserId(long userId) {
    return jdbcTemplate.query(
        "select * from comment where userId = ?",
        this::mapRowToComment,
        userId);
  }

  @Override
  public Optional<Comment> getById(long commentId) {
    return jdbcTemplate.query(
        "select * from comment where commentId = ?",
        this::mapRowToComment,
        commentId).stream()
        .findFirst();
  }

  @Override
  public Optional<Comment> save(CommentDto commentDto) {
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("userId", commentDto.getUserId())
        .addValue("drinkId", commentDto.getDrinkId())
        .addValue("ranking", commentDto.getRanking())
        .addValue("commentBody", commentDto.getCommentBody());

    Number key = simpleJdbcInsert.executeAndReturnKey(params);
    return this.getById(key.longValue()).stream().findFirst();
  }

  @Override
  public Optional<Comment> update(long commentId, CommentDto commentDto) {
    String update = "update comment set userId = :userId, drinkId = :drinkId, ranking = :ranking, " +
        "commentBody = :commentBody where commentId = :commentId";
    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("commentId", commentId)
        .addValue("userId", commentDto.getUserId())
        .addValue("drinkId", commentDto.getDrinkId())
        .addValue("ranking", commentDto.getRanking())
        .addValue("commentBody", commentDto.getCommentBody());

    int numChanged = namedParameterJdbcTemplate.update(update, params);
    if (numChanged > 0) {
      return this.getById(commentId);
    } else {
      return Optional.empty();
    }

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

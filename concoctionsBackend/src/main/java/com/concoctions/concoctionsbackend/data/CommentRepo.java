package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepo {

  List<Comment> getAllComments();
  List<Comment> getCommentsByDrinkId(long drinkId);
  List<Comment> getCommentsByUserId(long userId);
  Optional<Comment> getCommentById(long commentId);
  int deleteByID(long commentId);
}

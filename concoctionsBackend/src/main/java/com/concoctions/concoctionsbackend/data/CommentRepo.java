package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.model.Comment;

import java.util.List;

public interface CommentRepo {

  List<Comment> getAllComments();
  List<Comment> getCommentsByDrinkId(long drinkId);
  List<Comment> getCommentsByUserId(long userId);
  Comment getCommentById(long commentId);
}

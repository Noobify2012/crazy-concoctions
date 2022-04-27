package com.concoctions.concoctionsbackend.data;

import com.concoctions.concoctionsbackend.dto.CommentDto;
import com.concoctions.concoctionsbackend.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepo {

  List<Comment> getAll();
  List<Comment> getAllByDrinkId(long drinkId);
  List<Comment> getAllByUserId(long userId);
  Optional<Comment> getById(long commentId);
  Optional<Comment> save(CommentDto commentDto);
  Optional<Comment> update(long commentId, CommentDto commentDto);
  int deleteByID(long commentId);
}

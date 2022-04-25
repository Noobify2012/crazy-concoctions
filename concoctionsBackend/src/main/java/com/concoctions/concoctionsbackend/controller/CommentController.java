package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.CommentRepo;
import com.concoctions.concoctionsbackend.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

  private final CommentRepo commentRepo;

  @Autowired
  public CommentController(CommentRepo commentRepo) {
    this.commentRepo = commentRepo;
  }

  @GetMapping("/all")
  public List<Comment> allComments() {
    return commentRepo.getAllComments();
  }

  @GetMapping("/find")
  public ResponseEntity<List<Comment>> findComment(
      @RequestParam Optional<Long> drinkId,
      @RequestParam Optional<Long> userId,
      @RequestParam Optional<Long> commentId
  ){
    List<Comment> comments;
    if(drinkId.isPresent()) {
      comments = commentRepo.getCommentsByDrinkId(drinkId.get());
    } else if (userId.isPresent()) {
      comments = commentRepo.getCommentsByUserId(userId.get());
    } else if (commentId.isPresent()) {
      comments = List.of(commentRepo.getCommentById(commentId.get()));
      //todo error check that comment actually returned something
    } else {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(comments);
  }
}

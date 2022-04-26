package com.concoctions.concoctionsbackend.controller;

import com.concoctions.concoctionsbackend.data.CommentRepo;
import com.concoctions.concoctionsbackend.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
      @RequestParam Optional<Long> userId
      // this is clunky
  ){
    List<Comment> comments;
    if(drinkId.isPresent()) {
      comments = commentRepo.getCommentsByDrinkId(drinkId.get());
    } else if (userId.isPresent()) {
      comments = commentRepo.getCommentsByUserId(userId.get());
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    return ResponseEntity
        .status(HttpStatus.ACCEPTED)
        .body(comments);
  }

  @GetMapping("/find/byId")
  public ResponseEntity<Comment> findCommentById(
      @RequestParam long commentId
  ){
    // todo need a better error return when no commentId provided
    Optional<Comment> comment = commentRepo.getCommentById(commentId);
    return comment.map(value -> ResponseEntity.ok(comment.get()))
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(null)
        );
  }

  @DeleteMapping("/delete/{commentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteComment(
     @PathVariable long commentId
  ) {
    commentRepo.deleteByID(commentId);
  }

}

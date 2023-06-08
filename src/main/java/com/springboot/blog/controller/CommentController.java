package com.springboot.blog.controller;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CommentController {
  private CommentService commentService;

  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  @PostMapping("/posts/{postId}/comments")
  public ResponseEntity<CommentDto> createComment(
    @PathVariable Long postId, @Valid @RequestBody CommentDto commentDto) {

    return new ResponseEntity<>(this.commentService.createComment(postId, commentDto), HttpStatus.CREATED);
  }
  @GetMapping("/posts/{postId}/comments")
  public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable Long postId) {
    return ResponseEntity.ok(this.commentService.getCommentsByPostId(postId));
  }
  @GetMapping("/posts/{postId}/comments/{commentId}")
  public ResponseEntity<CommentDto> getCommentById(@PathVariable Long postId, @PathVariable Long commentId) {
    return ResponseEntity.ok(this.commentService.getCommentById(postId, commentId));
  }

  @PutMapping("/posts/{postId}/comments/{commentId}")
  public ResponseEntity<CommentDto> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @Valid @RequestBody CommentDto commentDto) {
    return ResponseEntity.ok(this.commentService.updateComment(postId, commentId, commentDto));
  }
  @DeleteMapping("/posts/{postId}/comments/{commentId}")
  public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
    this.commentService.deleteCommentById(postId, commentId);
    return ResponseEntity.ok("Comment is deleted");
  }
}

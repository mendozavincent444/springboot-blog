package com.springboot.blog.controller;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.service.PostService;
import com.springboot.blog.utils.AppConstants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping
public class PostController {

  private PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/api/v1/posts")
  public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
    return new ResponseEntity<>(this.postService.createPost(postDto), HttpStatus.CREATED);
  }

  @GetMapping("/api/v1/posts")
  public ResponseEntity<PostResponse> getAllPosts(
      @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
      @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
      @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
      @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
  ) {
    return ResponseEntity.ok(this.postService.getAllPosts(pageNo, pageSize, sortBy, sortDir));
  }

  @GetMapping(value = "/api/v1/posts/{id}")
  public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {

    return ResponseEntity.ok(this.postService.getPostById(id));

  }


  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("/api/v1/posts/{id}")
  public ResponseEntity<PostDto> updatePost(@Valid @RequestBody PostDto postDto, @PathVariable Long id) {

    PostDto postResponse = this.postService.updatePost(postDto, id);

    return ResponseEntity.ok(postResponse);

  }
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping("/api/v1/posts/{id}")
  public ResponseEntity<String> deletePost(@PathVariable Long id) {
    this.postService.deletePost(id);

    return ResponseEntity.ok("Post Successfully Deleted");
  }

  @GetMapping("/category/{id}")
  public ResponseEntity<List<PostDto>> getPostsByCategory(@PathVariable Long categoryId) {
    return ResponseEntity.ok(this.postService.getPostByCategoryId(categoryId));

  }
}

package com.springboot.blog.service.impl;

import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Comment;
import com.springboot.blog.model.Post;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

  private CommentRepository commentRepository;
  private PostRepository postRepository;
  private ModelMapper modelMapper;
  public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper modelMapper) {
    this.commentRepository = commentRepository;
    this.postRepository = postRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public CommentDto createComment(Long postId, @Valid CommentDto commentDto) {

    Comment comment = this.mapToEntity(commentDto);

    Post post = this.postRepository
      .findById(postId)
      .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    comment.setPost(post);

    Comment newComment = this.commentRepository.save(comment);

    return mapToDTO(newComment);
  }

  @Override
  public List<CommentDto> getCommentsByPostId(Long postId) {

    List<Comment> comments = this.commentRepository.findByPostId(postId);

    return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
  }

  @Override
  public CommentDto getCommentById(Long postId, Long commentId) {
    Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    if (!comment.getPost().getId().equals(post.getId())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
    }

    return this.mapToDTO(comment);
  }

  @Override
  public CommentDto updateComment(Long postId, Long commentId, @Valid CommentDto commentDto) {
    Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    if (!comment.getPost().getId().equals(post.getId())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
    }

    comment.setName(commentDto.getName());
    comment.setEmail(commentDto.getEmail());
    comment.setBody(commentDto.getBody());

    Comment updatedComment = this.commentRepository.save(comment);

    return mapToDTO(updatedComment);

  }

  @Override
  public void deleteCommentById(Long postId, Long commentId) {
    Post post = this.postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

    Comment comment = this.commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentId));

    if (!comment.getPost().getId().equals(post.getId())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
    }

    this.commentRepository.delete(comment);

  }

  private CommentDto mapToDTO(Comment comment) {
    CommentDto commentDto = this.modelMapper.map(comment, CommentDto.class);
    return commentDto;
  }

  private Comment mapToEntity(CommentDto commentDto) {
    Comment comment = this.modelMapper.map(commentDto, Comment.class);
    return comment;
  }
}

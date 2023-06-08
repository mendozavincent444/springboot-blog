package com.springboot.blog.service.impl;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Category;
import com.springboot.blog.model.Post;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

  private PostRepository postRepository;
  private CategoryRepository categoryRepository;
  private ModelMapper modelMapper;

  public PostServiceImpl(PostRepository postRepository, CategoryRepository categoryRepository, ModelMapper modelMapper) {
    this.postRepository = postRepository;
    this.categoryRepository = categoryRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public PostDto createPost(PostDto postDto) {

    Category category = this.categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() ->
      new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));

    Post post = mapToEntity(postDto);

    post.setCategory(category);
    Post newPost = this.postRepository.save(post);

    PostDto postResponse = mapToDTO(newPost);

    return postResponse;
  }



  @Override
  public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

    Sort sort = sortDir
      .equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

    Page<Post> posts = this.postRepository.findAll(pageable);

    List<Post> listOfPosts = posts.getContent();

    List<PostDto> content = listOfPosts.stream().map(post -> mapToDTO(post)).collect(Collectors.toList());

    PostResponse postResponse = new PostResponse();
    postResponse.setContent(content);
    postResponse.setPageNo(posts.getNumber());
    postResponse.setPageSize(posts.getSize());
    postResponse.setTotalElements(posts.getTotalElements());
    postResponse.setTotalPages(posts.getTotalPages());
    postResponse.setLast(posts.isLast());

    return postResponse;
  }

  @Override
  public PostDto getPostById(Long id) {
    Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

    return mapToDTO(post);
  }

  @Override
  public PostDto updatePost(PostDto postDto, Long id) {
    Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

    Category category = this.categoryRepository.findById(postDto.getCategoryId()).orElseThrow(() ->
      new ResourceNotFoundException("Category", "id", postDto.getCategoryId()));


    post.setTitle(postDto.getTitle());
    post.setDescription(postDto.getDescription());
    post.setContent(postDto.getContent());
    post.setCategory(category);

    Post updatedPost = this.postRepository.save(post);

    return mapToDTO(updatedPost);
  }

  @Override
  public void deletePost(Long id) {

    Post post = this.postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Post", "id", id));

    this.postRepository.delete(post);
  }

  @Override
  public List<PostDto> getPostByCategoryId(Long categoryId) {
    Category category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

    List<Post> posts = this.postRepository.findByCategoryId(categoryId);

    return posts.stream().map((post -> this.modelMapper.map(post, PostDto.class))).collect(Collectors.toList());
  }


  private Post mapToEntity(PostDto postDto) {
    Post post = modelMapper.map(postDto, Post.class);
    return post;
  }

  private PostDto mapToDTO(Post post) {
    PostDto postDto = modelMapper.map(post, PostDto.class);
    return postDto;
  }
}

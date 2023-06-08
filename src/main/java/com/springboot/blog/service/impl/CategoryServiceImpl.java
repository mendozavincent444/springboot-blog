package com.springboot.blog.service.impl;

import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.model.Category;
import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.repository.CategoryRepository;
import com.springboot.blog.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

  private CategoryRepository categoryRepository;
  private ModelMapper modelMapper;

  public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
    this.categoryRepository = categoryRepository;
    this.modelMapper = modelMapper;
  }

  @Override
  public CategoryDto addCategory(CategoryDto categoryDto) {
    Category category = modelMapper.map(categoryDto, Category.class);

    Category savedCategory = this.categoryRepository.save(category);

    return this.modelMapper.map(savedCategory, CategoryDto.class);
  }

  @Override
  public CategoryDto getCategoryById(Long id) {
    Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

    return this.modelMapper.map(category, CategoryDto.class);
  }

  @Override
  public List<CategoryDto> getAllCategories() {
     List<Category> categories = this.categoryRepository.findAll();

     List<CategoryDto> categoriesDto = categories.stream().map((category ->
       this.modelMapper.map(category, CategoryDto.class))).collect(Collectors.toList());

     return categoriesDto;
  }

  @Override
  public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
    Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

    category.setName(categoryDto.getName());
    category.setDescription(categoryDto.getDescription());

    return this.modelMapper.map(category, CategoryDto.class);
  }

  public void deleteCategoryById(Long id) {
    Category category = this.categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

    this.categoryRepository.delete(category);
  }
}

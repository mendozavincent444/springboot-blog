package com.springboot.blog.service;

import com.springboot.blog.payload.CategoryDto;

import java.util.List;

public interface CategoryService {

  CategoryDto addCategory(CategoryDto categoryDto);

  CategoryDto getCategoryById(Long id);

  List<CategoryDto> getAllCategories();

  CategoryDto updateCategory(Long id, CategoryDto categoryDto);

  void deleteCategoryById(Long id);
}

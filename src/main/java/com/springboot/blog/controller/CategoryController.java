package com.springboot.blog.controller;



import com.springboot.blog.payload.CategoryDto;
import com.springboot.blog.service.CategoryService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

  private CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
    CategoryDto savedCategory = this.categoryService.addCategory(categoryDto);
    return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
  }

  @GetMapping("{id}")
  public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
    return ResponseEntity.ok(this.categoryService.getCategoryById(id));
  }

  @GetMapping
  public ResponseEntity<List<CategoryDto>> getAllCategories() {
    return ResponseEntity.ok(this.categoryService.getAllCategories());
  }

  @PreAuthorize("hasRole('ADMIN')")
  @PutMapping("{id}")
  public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
    return ResponseEntity.ok(this.categoryService.updateCategory(id, categoryDto));
  }
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
    this.categoryService.deleteCategoryById(id);

    return ResponseEntity.ok("Category successfully deleted");

  }
}

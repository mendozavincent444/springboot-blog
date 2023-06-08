package com.springboot.blog.payload;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CategoryDto {
  private Long id;
  private String name;
  private String description;
}

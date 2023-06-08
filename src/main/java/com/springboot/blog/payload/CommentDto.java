package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDto {
  private Long id;
  @NotEmpty(message = "Name should not be empty")
  private String name;
  @NotEmpty(message = "Email should not be empty")
  @Email
  private String email;
  @NotEmpty
  @Size(min = 10, message = "Comment must be minimum of 10 characters")
  private String body;

}

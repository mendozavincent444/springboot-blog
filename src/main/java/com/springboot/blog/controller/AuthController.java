package com.springboot.blog.controller;

import com.springboot.blog.payload.JWTAuthResponse;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  private AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  // Build login REST API

  @PostMapping("/login")
  public ResponseEntity<JWTAuthResponse> login(@RequestBody LoginDto loginDto) {
    String token = this.authService.login(loginDto);

    JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
    jwtAuthResponse.setAccesstoken(token);


    return ResponseEntity.ok(jwtAuthResponse);
  }

  // Buil register REST API
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
    String response = this.authService.register(registerDto);
    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }
}

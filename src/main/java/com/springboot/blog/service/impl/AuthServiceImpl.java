package com.springboot.blog.service.impl;

import com.springboot.blog.exception.BlogAPIException;
import com.springboot.blog.model.Role;
import com.springboot.blog.model.User;
import com.springboot.blog.payload.LoginDto;
import com.springboot.blog.payload.RegisterDto;
import com.springboot.blog.repository.RoleRepository;
import com.springboot.blog.repository.UserRepository;
import com.springboot.blog.security.JwtProvider;
import com.springboot.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
  private AuthenticationManager authenticationManager;
  private UserRepository userRepository;
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;
  private JwtProvider jwtProvider;

  public AuthServiceImpl(AuthenticationManager authenticationManager,
                         UserRepository userRepository,
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder,
                         JwtProvider jwtProvider) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtProvider = jwtProvider;
  }

  @Override
  public String login(LoginDto loginDto) {

    Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      loginDto.getUsernameOrEmail(), loginDto.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = this.jwtProvider.generateToken(authentication);

    return token;
  }

  @Override
  public String register(RegisterDto registerDto) {
    if (this.userRepository.existsByUsername(registerDto.getUsername())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Username already exists!");
    }


    if (this.userRepository.existsByEmail(registerDto.getEmail())) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Email already exists!");
    }

    User user = new User();
    user.setName(registerDto.getName());
    user.setUsername(registerDto.getUsername());
    user.setEmail(registerDto.getEmail());
    user.setPassword(this.passwordEncoder.encode(registerDto.getPassword()));

    Set<Role> roles = new HashSet<>();
    Role userRole = this.roleRepository.findByName("ROLE_USER").get();

    roles.add(userRole);
    user.setRoles(roles);
    userRepository.save(user);

    return "User registered successfully!";

  }
}

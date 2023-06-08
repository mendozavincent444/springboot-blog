package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
  @Value("${app.jwt-secret}")
  private String jwtSecret;
  @Value("${app-jwt-expiration-milliseconds}")
  private long jwtExpirationDate;

  // generate JWT
  public String generateToken(Authentication authentication) {
    String username = authentication.getName();

    Date currentDate = new Date();

    Date expireDate = new Date(currentDate.getTime() + this.jwtExpirationDate);

    String token = Jwts.builder()
      .setSubject(username)
      .setIssuedAt(new Date())
      .setExpiration(expireDate)
      .signWith(this.key())
      .compact();

    return token;
  }


  private Key key() {
    return Keys.hmacShaKeyFor(
      Decoders.BASE64.decode(jwtSecret)
    );
  }


  //get username from JWT
  public String getUsername(String token) {
    Claims claims = Jwts.parserBuilder()
      .setSigningKey(this.key())
      .build()
      .parseClaimsJws(token)
      .getBody();

    String username = claims.getSubject();
    return username;
  }

  public boolean validateToken(String token) {

    try {

      Jwts.parserBuilder()
        .setSigningKey(this.key())
        .build()
        .parse(token);

      return true;

    } catch (ExpiredJwtException e) {

      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT Token");

    } catch (MalformedJwtException e) {

      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");

    } catch (SignatureException e) {

      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");

    } catch (IllegalArgumentException e) {

      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty");

    }

  }



}

package com.job_tracker.controller;

import com.job_tracker.dto.*;
import com.job_tracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> userToCreate(
      @RequestBody @Valid UserCreateRequestDto user) {
    UserResponseDto created = userService.userToCreate(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> userToLogin(@RequestBody @Valid RequestLoginDto user) {
    LoginResponseDto login = userService.userToLogin(user);
    return ResponseEntity.ok(login);
  }

  @PutMapping("/me")
  public ResponseEntity<UserResponseDto> userToUpdate(@RequestBody UserUpdateDto user) {
    UserResponseDto userResponseDto = userService.userToUpdate(user);
    return ResponseEntity.ok(userResponseDto);
  }

  @GetMapping("/users")
  public ResponseEntity<Page<UserResponseDto>> getAllUsers(
      @ModelAttribute PaginationParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size(), Sort.by(params.sortBy()));
    Page<UserResponseDto> users = userService.getAllUsers(pageable);
    return ResponseEntity.ok(users);
  }

  @GetMapping("/users/email/{email}")
  public ResponseEntity<UserResponseDto> getUserByEmail(@PathVariable("email") String email) {
    UserResponseDto userByEmail = userService.getUserByEmail(email);
    return ResponseEntity.ok(userByEmail);
  }

  @DeleteMapping("/users/email/{email}")
  public ResponseEntity<UserResponseDto> deleteUserByEmail(@PathVariable("email") String email) {
    return ResponseEntity.ok(userService.deleteUser(email));
  }

  @PutMapping("/me/password")
  public ResponseEntity<Void> userUpdatePassword(
      @RequestBody @Valid UserUpdatePasswordRequestDto passwordUpdate) {
    userService.userUpdatePassword(passwordUpdate);
    return ResponseEntity.ok().build();
  }
}

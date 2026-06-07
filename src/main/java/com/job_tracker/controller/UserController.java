package com.job_tracker.controller;

import com.job_tracker.dto.*;
import com.job_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

  public UserController(UserService service) {
    this.userService = service;
  }

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
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "20") int size,
          @RequestParam(defaultValue = "id") String sortBy
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    org.springframework.data.domain.Page<UserResponseDto> users = userService.getAllUsers(pageable);
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
  public ResponseEntity<Void> userUpdatePassword(@RequestBody @Valid UserUpdatePasswordRequestDto passwordUpdate){
    userService.userUpdatePassword(passwordUpdate);
    return ResponseEntity.ok().build();
  }

}

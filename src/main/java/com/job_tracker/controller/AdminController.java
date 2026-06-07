package com.job_tracker.controller;

import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

  private final AdminService adminService;

  public AdminController(AdminService adminService) {
    this.adminService = adminService;
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> createAdmin(
      @RequestBody @Valid UserCreateRequestDto user) {
    UserResponseDto created = adminService.createAdmin(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
}

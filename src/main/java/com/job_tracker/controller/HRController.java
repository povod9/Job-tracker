package com.job_tracker.controller;

import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.service.HRService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hr")
public class HRController {

  private final HRService service;

  public HRController(HRService service) {
    this.service = service;
  }

  @PostMapping("/register")
  public ResponseEntity<UserResponseDto> createHR(
      @RequestBody @Valid UserCreateRequestDto userCreateRequestDto) {
    UserResponseDto userResponseDto = service.createHR(userCreateRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);
  }
}

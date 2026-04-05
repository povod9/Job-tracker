package com.job_tracker.controller;

import com.job_tracker.dto.*;
import com.job_tracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService service) {
        this.userService = service;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> userToCreate(
            @RequestBody @Valid UserCreateRequestDto user
    )
    {
        UserResponseDto created = userService.userToCreate(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userToLogin(
            @RequestBody @Valid RequestLoginDto user
    )
    {
        LoginResponseDto login = userService.userToLogin(user);
        return ResponseEntity
                .ok(login);
    }

    @PutMapping("/me/update/information")
    public ResponseEntity<UserResponseDto> userToUpdate(
            @RequestBody UserUpdateDto user
    )
    {
        UserResponseDto userResponseDto = userService.userToUpdate(user);
        return ResponseEntity
                .ok(userResponseDto);
    }
}

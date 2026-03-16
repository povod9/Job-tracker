package com.job_tracker.Controller;

import com.job_tracker.Dto.*;
import com.job_tracker.Service.UserService;
import jakarta.validation.Valid;
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
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userToLogin(
            @RequestBody @Valid RequestLoginDto user
    )
    {
        LoginResponseDto login = userService.userToLogin(user);
        return ResponseEntity.ok(login);
    }

    @PutMapping("/me/update-info")
    public ResponseEntity<UserResponseDto> userToUpdate(
            @RequestBody UserUpdateDto user
    )
    {
        UserResponseDto userResponseDto = userService.userToUpdate(user);
        return ResponseEntity.ok(userResponseDto);
    }
}

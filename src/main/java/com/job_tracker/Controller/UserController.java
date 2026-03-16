package com.job_tracker.Controller;

import com.job_tracker.Dto.LoginResponseDto;
import com.job_tracker.Dto.RequestLoginDto;
import com.job_tracker.Dto.UserCreateRequestDto;
import com.job_tracker.Dto.UserResponseDto;
import com.job_tracker.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponseDto> userToCreate(
            @RequestBody @Valid UserCreateRequestDto user
    )
    {
        UserResponseDto created = service.userToCreate(user);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> userToLogin(
            @RequestBody @Valid RequestLoginDto user
    )
    {
        LoginResponseDto login = service.userToLogin(user);
        return ResponseEntity.ok(login);
    }

    @GetMapping("/by-email")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @RequestParam String email
    )
    {
        UserResponseDto userByEmail = service.getUserByEmail(email);
        return ResponseEntity.ok(userByEmail);
    }

    @PostMapping("/admin")
    public ResponseEntity<UserResponseDto> createAdmin(
            @RequestBody @Valid UserCreateRequestDto user
    )
    {
        UserResponseDto created = service.createAdmin(user);
        return ResponseEntity.ok(created);
    }
}

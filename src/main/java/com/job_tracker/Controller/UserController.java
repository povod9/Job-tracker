package com.job_tracker.Controller;

import com.job_tracker.Dto.*;
import com.job_tracker.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/me/update-info")
    public ResponseEntity<UserResponseDto> userToUpdate(
            @RequestBody UserUpdateDto user
    )
    {
        UserResponseDto userResponseDto = userService.userToUpdate(user);
        return ResponseEntity
                .ok(userResponseDto);
    }

    @PostMapping("/create/application")
    public ResponseEntity<ApplicationResponseDto> createApplication(
            @RequestBody ApplicationCreateRequestDto application
    )
    {
        ApplicationResponseDto applicationResponseDto = userService.createApplication(application);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationResponseDto);
    }

    @GetMapping("/me/application")
    public ResponseEntity<List<ApplicationResponseDto>> getMyApplication()
    {
        List<ApplicationResponseDto> applicationResponseDto = userService.getMyApplication();
        return ResponseEntity
                .ok(applicationResponseDto);
    }

    @DeleteMapping("/me/delete-my-application/{id}")
    public ResponseEntity<ApplicationResponseDto> deleteMyApplicationById(
            @PathVariable("id") Long id
    )
    {
        ApplicationResponseDto deletedApplicationResponseDto = userService.deleteMyApplicationById(id);
        return ResponseEntity
                .ok(deletedApplicationResponseDto);
    }

    //@PutMapping("/me/update-application-status")

}

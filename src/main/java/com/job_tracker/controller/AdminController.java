package com.job_tracker.controller;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> createAdmin(
            @RequestBody @Valid UserCreateRequestDto user
    )
    {
        UserResponseDto created = adminService.createAdmin(user);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDto>> getAllUser()
    {
        return ResponseEntity
                .ok(adminService.getAllUser());
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @PathVariable("{email}") String email
    )
    {
        UserResponseDto userByEmail = adminService.getUserByEmail(email);
        return ResponseEntity
                .ok(userByEmail);
    }

    @DeleteMapping("/users/delete/email/{email}")
    public ResponseEntity<UserResponseDto> deleteUserByEmail(
            @PathVariable("{email}") String email
    )
    {
        return ResponseEntity
                .ok(adminService.deleteUser(email));
    }

    @GetMapping("/deleted/applications")
    public  ResponseEntity<List<ApplicationResponseDto>> getDeletedApplication()
    {
        List<ApplicationResponseDto> applicationResponseDtoList = adminService.getDeletedApplication();
        return ResponseEntity
                .ok(applicationResponseDtoList);
    }

    @GetMapping("/activities")
    public ResponseEntity<List<ActivityEventResponseDto>> getAllActivityEvent()
    {
        List<ActivityEventResponseDto> activityEventResponseDto = adminService.getAllActivityEvent();
        return ResponseEntity
                .ok(activityEventResponseDto);
    }
}

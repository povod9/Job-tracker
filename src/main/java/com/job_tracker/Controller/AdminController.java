package com.job_tracker.Controller;

import com.job_tracker.Dto.ApplicationResponseDto;
import com.job_tracker.Dto.UserCreateRequestDto;
import com.job_tracker.Dto.UserResponseDto;
import com.job_tracker.Service.AdminService;
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

    @GetMapping("/find/all-user")
    public ResponseEntity<List<UserResponseDto>> getAllUser()
    {
        return ResponseEntity
                .ok(adminService.getAllUser());
    }

    @GetMapping("/find/by-email")
    public ResponseEntity<UserResponseDto> getUserByEmail(
            @RequestParam String email
    )
    {
        UserResponseDto userByEmail = adminService.getUserByEmail(email);
        return ResponseEntity
                .ok(userByEmail);
    }

    @DeleteMapping("/delete/by-email")
    public ResponseEntity<UserResponseDto> deleteUserByEmail(
            @RequestParam String email
    )
    {
        return ResponseEntity
                .ok(adminService.deleteUser(email));
    }

    @GetMapping("/deleted/application")
    public  ResponseEntity<List<ApplicationResponseDto>> getDeletedApplication()
    {
        List<ApplicationResponseDto> applicationResponseDtoList = adminService.getDeletedApplication();
        return ResponseEntity
                .ok(applicationResponseDtoList);
    }

    @GetMapping("/find/all-activity-event")
    public ResponseEntity<>
}

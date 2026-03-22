package com.job_tracker.Dto;

import com.job_tracker.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserResponseDto(
        @NotBlank
        String name,
        @Email
        @NotBlank
        String email,
        @NotBlank
        Role role
) {
}

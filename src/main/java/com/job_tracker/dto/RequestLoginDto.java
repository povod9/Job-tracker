package com.job_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RequestLoginDto(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String passwordHash
) {
}

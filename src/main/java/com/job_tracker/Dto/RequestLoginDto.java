package com.job_tracker.Dto;

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

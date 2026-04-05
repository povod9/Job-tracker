package com.job_tracker.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PrincipalDto(
        @Email
        @NotBlank
        String email,
        @NotNull
        String role,
        @NotBlank
        Long id
) {
}

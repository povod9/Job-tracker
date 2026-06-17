package com.job_tracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdatePasswordRequestDto(
    @NotNull
        @Size(
            min = 5,
            max = 50,
            message =
                """
                Password cannot be smaller than 5 symbols
                and bigger than 50 symbols
                """)
        String currentPassword,
    @NotNull
        @Size(
            min = 5,
            max = 50,
            message =
                """
                Password cannot be smaller than 5 symbols
                and bigger than 50 symbols
                """)
        String newPassword) {}

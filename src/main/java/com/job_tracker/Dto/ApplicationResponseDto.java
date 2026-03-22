package com.job_tracker.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.OffsetDateTime;

public record ApplicationResponseDto(

        @NotBlank
        UserResponseDto userDto,
        @NotBlank
        String company,
        @NotBlank
        String position,
        @NotNull
        OffsetDateTime createdAt,
        @NotNull
        OffsetDateTime updatedAt,
        @NotNull
        Long version
) {
}

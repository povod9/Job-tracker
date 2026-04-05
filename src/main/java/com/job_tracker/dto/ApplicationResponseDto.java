package com.job_tracker.dto;

import com.job_tracker.enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import java.time.OffsetDateTime;

public record ApplicationResponseDto(

        Long id,
        @NotBlank
        UserResponseDto userDto,
        @NotBlank
        String company,
        @NotBlank
        String position,
        @NotBlank
        ApplicationStatus applicationStatus,
        @NotNull
        OffsetDateTime createdAt,
        @NotNull
        OffsetDateTime updatedAt,
        @NotNull
        Long version
) {
}

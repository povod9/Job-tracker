package com.job_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record ReminderCreateRequestDto(
        @NotNull
        OffsetDateTime dueAt,
        @NotBlank
        String message
) {
}

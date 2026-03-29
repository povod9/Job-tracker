package com.job_tracker.Dto;

import com.job_tracker.Enums.ReminderStatus;

import java.time.OffsetDateTime;

public record ReminderResponseDto(
        Long id,
        ReminderApplicationResponseDto reminderApplicationResponseDto,
        OffsetDateTime dueAt,
        ReminderStatus reminderStatus,
        String message,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}

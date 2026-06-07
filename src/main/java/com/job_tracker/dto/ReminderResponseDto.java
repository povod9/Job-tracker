package com.job_tracker.dto;

import com.job_tracker.enums.ReminderStatus;
import java.time.OffsetDateTime;

public record ReminderResponseDto(
    Long id,
    ReminderApplicationResponseDto reminderApplicationResponseDto,
    OffsetDateTime dueAt,
    ReminderStatus status,
    String message,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt) {}

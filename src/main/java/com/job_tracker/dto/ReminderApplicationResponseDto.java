package com.job_tracker.dto;

public record ReminderApplicationResponseDto(
        Long applicationId,
        String company,
        String position
) {
}

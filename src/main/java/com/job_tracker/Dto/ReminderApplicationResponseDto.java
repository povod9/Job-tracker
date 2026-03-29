package com.job_tracker.Dto;

public record ReminderApplicationResponseDto(
        Long applicationId,
        String company,
        String position
) {
}

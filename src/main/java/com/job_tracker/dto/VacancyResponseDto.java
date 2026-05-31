package com.job_tracker.dto;


public record VacancyResponseDto(
        Long id,
        String company,
        String position,
        String description
) {
}

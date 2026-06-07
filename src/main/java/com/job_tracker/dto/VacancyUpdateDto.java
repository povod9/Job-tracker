package com.job_tracker.dto;


public record VacancyUpdateDto(
        String company,
        String position,
        String description
) {
}

package com.job_tracker.dto;


import com.job_tracker.enums.VacancyStatus;

public record VacancyResponseDto(
        Long id,
        String company,
        String position,
        String description,
        VacancyStatus status
) {
}

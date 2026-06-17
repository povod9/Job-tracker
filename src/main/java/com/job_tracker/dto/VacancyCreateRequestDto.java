package com.job_tracker.dto;

import jakarta.validation.constraints.NotNull;

public record VacancyCreateRequestDto(
    @NotNull String company, @NotNull String position, @NotNull String description) {}

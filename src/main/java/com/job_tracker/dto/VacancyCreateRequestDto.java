package com.job_tracker.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record VacancyCreateRequestDto(
        @NotNull String company, @NotNull String position, @NotNull String description,
        BigDecimal salaryMax, BigDecimal salaryMin, List<String> location) {}

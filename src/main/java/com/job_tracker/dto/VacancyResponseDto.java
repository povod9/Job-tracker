package com.job_tracker.dto;

import com.job_tracker.enums.VacancySource;
import com.job_tracker.enums.VacancyStatus;
import java.math.BigDecimal;
import java.util.List;

public record VacancyResponseDto(
        Long id, String company, String position, String description, VacancyStatus status, VacancySource source,
        BigDecimal salaryMax, BigDecimal salaryMin, List<String> location, String redirectUrl) {}

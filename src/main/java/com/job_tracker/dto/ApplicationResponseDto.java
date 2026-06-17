package com.job_tracker.dto;

import com.job_tracker.enums.ApplicationStatus;
import java.time.OffsetDateTime;

public record ApplicationResponseDto(
    Long id,
    UserResponseDto userDto,
    VacancyResponseDto vacancyDto,
    ApplicationStatus applicationStatus,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    Long version) {}

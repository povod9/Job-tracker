package com.job_tracker.dto;

import java.time.OffsetDateTime;

public record ActivityEventResponseDto(
        Long id,
        ApplicationResponseDto applicationDto,
        OffsetDateTime createdAt
) {
}

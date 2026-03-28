package com.job_tracker.Dto;

import java.time.OffsetDateTime;

public record ActivityEventResponseDto(
        Long id,
        ApplicationResponseDto applicationDto,
        OffsetDateTime createdAt
) {
}

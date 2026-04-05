package com.job_tracker.dto;

import java.time.OffsetDateTime;

public record ExceptionDto(
        String message,
        String errorMessage,
        OffsetDateTime time
) {
}

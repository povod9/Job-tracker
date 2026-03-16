package com.job_tracker.Dto;

import java.time.OffsetDateTime;

public record ExceptionDto(
        String message,
        String errorMessage,
        OffsetDateTime time
) {
}

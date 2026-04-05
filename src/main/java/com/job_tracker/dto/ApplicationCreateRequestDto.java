package com.job_tracker.dto;

import jakarta.validation.constraints.NotBlank;

public record ApplicationCreateRequestDto(

        @NotBlank
        String company,
        @NotBlank
        String position

) {
}

package com.job_tracker.Dto;

import jakarta.validation.constraints.NotBlank;

public record ApplicationCreateRequestDto(

        @NotBlank
        String company,
        @NotBlank
        String position

) {
}

package com.job_tracker.dto;

import jakarta.validation.constraints.NotNull;

public record ApplicationCreateRequestDto(@NotNull Long vacancyId) {}

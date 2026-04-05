package com.job_tracker.dto;

public record UserUpdateDto(
        String name,
        String email,
        String passwordHash
) {
}

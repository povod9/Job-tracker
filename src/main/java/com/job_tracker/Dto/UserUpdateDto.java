package com.job_tracker.Dto;

public record UserUpdateDto(
        String name,
        String email,
        String passwordHash
) {
}

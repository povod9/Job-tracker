package com.job_tracker.Dto;

import com.job_tracker.Enums.Role;

public record UserResponseDto(
        String name,
        String email,
        Role role
) {
}

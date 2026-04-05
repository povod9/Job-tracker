package com.job_tracker.dto;


public record LoginResponseDto (
        String accessToken,
        String tokenType
){
}

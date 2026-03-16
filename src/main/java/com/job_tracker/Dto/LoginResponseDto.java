package com.job_tracker.Dto;


public record LoginResponseDto (
        String accessToken,
        String tokenType
){
}

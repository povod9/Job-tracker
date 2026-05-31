package com.job_tracker.service;

import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;


public interface HRService {
    UserResponseDto createHR(UserCreateRequestDto userCreateRequestDto);
}

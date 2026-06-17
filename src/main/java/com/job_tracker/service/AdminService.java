package com.job_tracker.service;

import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;

public interface AdminService {
  UserResponseDto createAdmin(UserCreateRequestDto user);
}

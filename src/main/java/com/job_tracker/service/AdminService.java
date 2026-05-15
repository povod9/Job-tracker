package com.job_tracker.service;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import java.util.List;

public interface AdminService {
  UserResponseDto createAdmin(UserCreateRequestDto user);

  List<UserResponseDto> getAllUser();

  UserResponseDto getUserByEmail(String email);

  UserResponseDto deleteUser(String email);

  List<ApplicationResponseDto> getDeletedApplication();

  List<ActivityEventResponseDto> getAllActivityEvent();
}

package com.job_tracker.service;

import com.job_tracker.dto.*;

public interface UserService {
  UserResponseDto userToCreate(UserCreateRequestDto user);

  LoginResponseDto userToLogin(RequestLoginDto user);

  UserResponseDto userToUpdate(UserUpdateDto user);
}

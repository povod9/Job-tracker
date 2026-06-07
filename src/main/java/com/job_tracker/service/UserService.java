package com.job_tracker.service;

import com.job_tracker.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
  UserResponseDto userToCreate(UserCreateRequestDto user);

  LoginResponseDto userToLogin(RequestLoginDto user);

  UserResponseDto userToUpdate(UserUpdateDto user);

  Page<UserResponseDto> getAllUsers(Pageable pageable);

  UserResponseDto getUserByEmail(String email);

  UserResponseDto deleteUser(String email);

  void userUpdatePassword(UserUpdatePasswordRequestDto passwordUpdate);
}

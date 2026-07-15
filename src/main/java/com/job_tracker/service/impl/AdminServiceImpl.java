package com.job_tracker.service.impl;

import com.job_tracker.dto.*;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.service.AdminService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public UserResponseDto createAdmin(UserCreateRequestDto user) {

    if (userRepository.existsByEmail(user.email())) {
      throw new IllegalArgumentException("Email already exists " + user.email());
    }

    UserEntity userEntity = UserEntity.builder()
            .name(user.name())
            .email(user.email())
            .password(passwordEncoder.encode(user.password()))
            .role(Role.ADMIN)
            .build();
    userEntity = userRepository.save(userEntity);

    return userMapper.userToDto(userEntity);
  }
}

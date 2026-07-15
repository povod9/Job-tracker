package com.job_tracker.service.impl;

import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.service.HRService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HRServiceImpl implements HRService {

  private final UserRepository repository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
  @Transactional
  public UserResponseDto createHR(UserCreateRequestDto userCreateRequestDto) {
    if (repository.existsByEmail(userCreateRequestDto.email())) {
      throw new IllegalArgumentException("Email already exists: " + userCreateRequestDto.email());
    }

    UserEntity createdUserEntity = UserEntity.builder()
            .name(userCreateRequestDto.name())
            .email(userCreateRequestDto.email())
            .password(passwordEncoder.encode(userCreateRequestDto.password()))
            .role(Role.HR)
            .build();

    UserEntity savedUser = repository.save(createdUserEntity);
    return mapper.userToDto(savedUser);
  }
}

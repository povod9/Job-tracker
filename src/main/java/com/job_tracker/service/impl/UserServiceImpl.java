package com.job_tracker.service.impl;

import com.job_tracker.create_exception.InvalidCredentialsException;
import com.job_tracker.dto.*;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.*;
import com.job_tracker.security.JwtCore;
import com.job_tracker.service.SecurityContextService;
import com.job_tracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper mapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtCore jwtCore;
  private final SecurityContextService securityContextService;

  @Override
  @Transactional
  public UserResponseDto userToCreate(UserCreateRequestDto user) {
    if (userRepository.existsByEmail(user.email())) {
      throw new IllegalArgumentException("Email already exists: " + user.email());
    }

    UserEntity userEntity =
        new UserEntity(
            null,
            user.name(),
            user.email(),
            passwordEncoder.encode(user.passwordHash()),
            Role.USER,
            null,
            null);

    userEntity = userRepository.save(userEntity);
    return mapper.userToDto(userEntity);
  }

  @Override
  public LoginResponseDto userToLogin(RequestLoginDto user) {
    UserEntity userEntity =
        userRepository
            .findByEmail(user.email())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find user by email " + user.email()));

    if (passwordEncoder.matches(user.passwordHash(), userEntity.getPasswordHash())) {
      return new LoginResponseDto(jwtCore.generateJwtToken(userEntity), "Bearer");
    } else {
      throw new InvalidCredentialsException("Wrong password or email");
    }
  }

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'HR')")
  @Transactional
  public UserResponseDto userToUpdate(UserUpdateDto user) {

    PrincipalDto principal = securityContextService.getCurrentPrincipalOrThrow();

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

    if (user.name() != null) {
      userEntity.setName(user.name());
    }

    if (user.email() != null) {
      if (user.email().equals(userEntity.getEmail())) {
        throw new IllegalArgumentException("Email cannot be the same");
      }

      if (userRepository.existsByEmail(user.email())) {
        throw new IllegalArgumentException("Email already exists");
      }

      userEntity.setEmail(user.email());
    }

    if (user.passwordHash() != null) {
      if (passwordEncoder.matches(user.passwordHash(), userEntity.getPasswordHash())) {
        throw new IllegalArgumentException("Password cannot be the same");
      }
      userEntity.setPasswordHash(passwordEncoder.encode(user.passwordHash()));
    }

    userEntity = userRepository.save(userEntity);
    return mapper.userToDto(userEntity);
  }
}

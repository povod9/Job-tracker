package com.job_tracker.service.impl;

import com.job_tracker.annotation.TrackExecutionTime;
import com.job_tracker.create_exception.InvalidCredentialsException;
import com.job_tracker.create_exception.InvalidPasswordException;
import com.job_tracker.create_exception.SamePasswordException;
import com.job_tracker.dto.*;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.*;
import com.job_tracker.security.JwtCore;
import com.job_tracker.service.SecurityContextService;
import com.job_tracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
            passwordEncoder.encode(user.password()),
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

    if (passwordEncoder.matches(user.password(), userEntity.getPassword())) {
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
    userEntity = userRepository.save(userEntity);
    return mapper.userToDto(userEntity);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional(readOnly = true)
  @TrackExecutionTime(unit = TimeUnit.MILLISECONDS, debug = false)
  public Page<UserResponseDto> getAllUsers(Pageable pageable) {
    return userRepository.findAll(pageable).map(mapper::userToDto);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional(readOnly = true)
  public UserResponseDto getUserByEmail(String email) {
    UserEntity userEntity =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user by email " + email));

    return mapper.userToDto(userEntity);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional
  public UserResponseDto deleteUser(String email) {
    UserEntity userEntity =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user by email " + email));

    userRepository.delete(userEntity);
    return mapper.userToDto(userEntity);
  }

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'HR')")
  @Transactional
  public void userUpdatePassword(UserUpdatePasswordRequestDto passwordUpdate) {
    PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();
    UserEntity userEntity =
        userRepository
            .findById(principalDto.id())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find user by id: " + principalDto.id()));

    if (!passwordEncoder.matches(passwordUpdate.currentPassword(), userEntity.getPassword())) {
      throw new InvalidPasswordException("Wrong current password");
    }

    if (passwordEncoder.matches(passwordUpdate.newPassword(), userEntity.getPassword())) {
      throw new SamePasswordException("The new password cannot be the same as the old");
    }

    userEntity.setPassword(passwordEncoder.encode(passwordUpdate.newPassword()));
  }
}

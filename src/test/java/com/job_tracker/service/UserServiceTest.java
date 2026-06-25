package com.job_tracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import com.job_tracker.dto.*;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.enums.VacancySource;
import com.job_tracker.enums.VacancyStatus;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.service.impl.UserServiceImpl;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock UserRepository userRepository;

  @Mock UserMapper userMapper;
  @Mock PasswordEncoder passwordEncoder;
  @InjectMocks UserServiceImpl userService;

  @Test
  void getUserByEmail() {
    UserEntity userEntity = createUserEntity();

    UserResponseDto expectedDto = createUserResponseDto();

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = userService.getUserByEmail(userEntity.getEmail());

    assertEquals(expectedDto, result);
  }

  @Test
  void deleteUser() {
    UserEntity userEntity = createUserEntity();

    UserResponseDto expectedDto = createUserResponseDto();

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = userService.deleteUser(userEntity.getEmail());

    verify(userRepository).delete(userEntity);
    assertEquals(expectedDto, result);
  }

  private VacancyEntity createVacancyEntity() {
    return new VacancyEntity(
        1L,
        null,
        createVacancyResponseDto().company(),
        createVacancyResponseDto().position(),
        createVacancyResponseDto().description(),
        createUserEntity(),
        VacancyStatus.ACTIVE,
        VacancySource.MANUAL,
        OffsetDateTime.now(),
        OffsetDateTime.now(),
        0L,
        BigDecimal.valueOf(2000),
        BigDecimal.valueOf(1000),
        null);
  }

  private UserEntity createUserEntity() {
    return new UserEntity(
        1L,
        "Jahn",
        "john@gmail.com",
        "SecretPassword",
        Role.USER,
        OffsetDateTime.now(),
        OffsetDateTime.now());
  }

  private VacancyResponseDto createVacancyResponseDto() {
    return new VacancyResponseDto(1L, "Nike", "Junior", "Description", VacancyStatus.ACTIVE);
  }

  private UserResponseDto createUserResponseDto() {
    return new UserResponseDto("Jahn", "john@gmail.com", Role.USER);
  }
  
}

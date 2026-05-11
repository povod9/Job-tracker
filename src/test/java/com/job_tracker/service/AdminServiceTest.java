package com.job_tracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.ApplicationStatus;
import com.job_tracker.enums.Role;
import com.job_tracker.mapper.ActivityMapper;
import com.job_tracker.mapper.ApplicationMapper;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.ActivityRepository;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.UserRepository;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

  @Mock UserRepository userRepository;
  @Mock ApplicationRepository applicationRepository;
  @Mock ActivityRepository activityRepository;
  @Mock ActivityMapper activityMapper;
  @Mock ApplicationMapper applicationMapper;
  @Mock UserMapper userMapper;
  @Mock PasswordEncoder passwordEncoder;
  @InjectMocks AdminService adminService;

  @Test
  void createAdmin() {
    String name = "Jack";
    String email = "jack@gmail.com";
    String passwordHash = "awfd32kir2mdlk";

    UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(name, email, passwordHash);

    when(passwordEncoder.encode(passwordHash)).thenReturn("secret_password");
    when(userRepository.existsByEmail(email)).thenReturn(false);

    adminService.createAdmin(userCreateRequestDto);

    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(userCaptor.capture());
    UserEntity userEntity = userCaptor.getValue();

    assertEquals(name, userEntity.getName());
    assertEquals(email, userEntity.getEmail());
    assertEquals("secret_password", userEntity.getPasswordHash());
    assertEquals(Role.ADMIN, userEntity.getRole());
  }

  @Test
  void dontCreateAdminIfEmailExists() {
    String name = "Jack";
    String email = "jack@gmail.com";
    String passwordHash = "awfd32kir2mdlk";

    UserCreateRequestDto userCreateRequestDto = new UserCreateRequestDto(name, email, passwordHash);

    when(userRepository.existsByEmail(email)).thenReturn(true);

    assertThrows(
        IllegalArgumentException.class, () -> adminService.createAdmin(userCreateRequestDto));

    verify(userRepository, never()).save(any(UserEntity.class));
    verify(passwordEncoder, never()).encode(anyString());
  }

  @Test
  void getAllUser() {

    UserEntity userEntity =
        new UserEntity(
            1L,
            "Jack1",
            "jack1@gmail.com",
            "awfd32kir2mdlk",
            Role.ADMIN,
            OffsetDateTime.now(),
            OffsetDateTime.now());

    UserEntity userEntity1 =
        new UserEntity(
            2L,
            "Jack2",
            "jack2@gmail.com",
            "awfd32kir2mdlk",
            Role.ADMIN,
            OffsetDateTime.now(),
            OffsetDateTime.now());

    UserEntity userEntity2 =
        new UserEntity(
            3L,
            "Jack3",
            "jack3@gmail.com",
            "awfd32kir2mdlk",
            Role.ADMIN,
            OffsetDateTime.now(),
            OffsetDateTime.now());

    List<UserEntity> userEntities = new ArrayList<>(List.of());

    userEntities.add(userEntity);
    userEntities.add(userEntity1);
    userEntities.add(userEntity2);

    UserResponseDto expectedDto =
        new UserResponseDto(userEntity.getName(), userEntity.getEmail(), userEntity.getRole());
    UserResponseDto expectedDto1 =
        new UserResponseDto(userEntity1.getName(), userEntity1.getEmail(), userEntity1.getRole());
    UserResponseDto expectedDto2 =
        new UserResponseDto(userEntity2.getName(), userEntity2.getEmail(), userEntity2.getRole());

    when(userRepository.findAll()).thenReturn(userEntities);
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);
    when(userMapper.userToDto(userEntity1)).thenReturn(expectedDto1);
    when(userMapper.userToDto(userEntity2)).thenReturn(expectedDto2);

    var value = adminService.getAllUser();

    assertEquals(3, value.size());
    assertEquals(expectedDto, value.getFirst());
    assertEquals(expectedDto1, value.get(1));
    assertEquals(expectedDto2, value.getLast());
  }

  @Test
  void getUserByEmail() {
    UserEntity userEntity =
        new UserEntity(
            1L,
            "Jack1",
            "jack1@gmail.com",
            "awfd32kir2mdlk",
            Role.USER,
            OffsetDateTime.now(),
            OffsetDateTime.now());

    UserResponseDto expectedDto =
        new UserResponseDto(userEntity.getName(), userEntity.getEmail(), userEntity.getRole());

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = adminService.getUserByEmail(userEntity.getEmail());

    assertEquals(expectedDto, result);
  }

  @Test
  void deleteUser() {
    UserEntity userEntity =
        new UserEntity(
            1L,
            "Jack1",
            "jack1@gmail.com",
            "awfd32kir2mdlk",
            Role.USER,
            OffsetDateTime.now(),
            OffsetDateTime.now());

    UserResponseDto expectedDto = new UserResponseDto("Jack", "jack3@gmail.com", Role.USER);

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = adminService.deleteUser(userEntity.getEmail());

    verify(userRepository).delete(userEntity);
    assertEquals(expectedDto, result);
  }

  @Test
  void getDeletedApplication() {

    UserEntity userEntity = new UserEntity();
    userEntity.setName("Jack");
    userEntity.setEmail("jack3@gmail.com");
    userEntity.setRole(Role.USER);

    ApplicationEntity applicationEntity = new ApplicationEntity();
    applicationEntity.setId(1L);
    applicationEntity.setUser(userEntity);
    applicationEntity.setCompany("Google");
    applicationEntity.setPosition("Junior");
    applicationEntity.setApplicationStatus(ApplicationStatus.DELETED);
    applicationEntity.setCreatedAt(OffsetDateTime.now());
    applicationEntity.setUpdatedAt(OffsetDateTime.now());
    applicationEntity.setVersion(1L);

    List<ApplicationEntity> applicationEntityList = List.of(applicationEntity, applicationEntity);

    UserResponseDto userResponseDto =
        new UserResponseDto(userEntity.getName(), userEntity.getEmail(), userEntity.getRole());

    List<ApplicationResponseDto> expectedApplicationList = new ArrayList<>();

    ApplicationResponseDto applicationResponseDto =
        new ApplicationResponseDto(
            1L,
            userResponseDto,
            applicationEntity.getCompany(),
            applicationEntity.getPosition(),
            ApplicationStatus.DELETED,
            applicationEntity.getCreatedAt(),
            applicationEntity.getUpdatedAt(),
            applicationEntity.getVersion());

    ApplicationResponseDto applicationResponseDto1 =
        new ApplicationResponseDto(
            1L,
            userResponseDto,
            applicationEntity.getCompany(),
            applicationEntity.getPosition(),
            ApplicationStatus.DELETED,
            applicationEntity.getCreatedAt(),
            applicationEntity.getUpdatedAt(),
            applicationEntity.getVersion());

    expectedApplicationList.add(applicationResponseDto);
    expectedApplicationList.add(applicationResponseDto1);

    when(applicationRepository.findByApplicationStatus(ApplicationStatus.DELETED))
        .thenReturn(applicationEntityList);
    when(applicationMapper.listApplicationToDto(applicationEntityList))
        .thenReturn(expectedApplicationList);

    var result = adminService.getDeletedApplication();

    assertEquals(2, result.size());
    assertEquals(expectedApplicationList.getFirst(), result.getFirst());
    assertEquals(expectedApplicationList.get(1), result.get(1));
  }

  @Test
  void getAllActivityEvent() {
    List<ActivityEventEntity> activityEventEntities = List.of(new ActivityEventEntity());

    UserResponseDto userResponseDto = new UserResponseDto("Jack", "jack3@gmail.com", Role.USER);

    ApplicationResponseDto applicationResponseDto =
        new ApplicationResponseDto(
            1L,
            userResponseDto,
            "Google",
            "Junior",
            ApplicationStatus.DRAFT,
            OffsetDateTime.now(),
            OffsetDateTime.now(),
            1L);

    List<ActivityEventResponseDto> activityEventResponseDtoList =
        List.of(new ActivityEventResponseDto(1L, applicationResponseDto, OffsetDateTime.now()));

    when(activityRepository.findAll()).thenReturn(activityEventEntities);
    when(activityMapper.activityEventEntityToActivityResponseDto(activityEventEntities))
        .thenReturn(activityEventResponseDtoList);

    var result = adminService.getAllActivityEvent();

    assertEquals(1, result.size());
    assertEquals(activityEventResponseDtoList.getFirst(), result.getFirst());
  }
}

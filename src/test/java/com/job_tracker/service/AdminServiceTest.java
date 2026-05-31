package com.job_tracker.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.job_tracker.dto.*;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.ApplicationStatus;
import com.job_tracker.enums.Role;
import com.job_tracker.enums.VacancyStatus;
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
import com.job_tracker.service.impl.AdminServiceImpl;
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
  @InjectMocks
  AdminServiceImpl adminService;

  @Test
  void createAdmin() {
    UserCreateRequestDto userCreateRequestDto = createUserCreateRequestDto();

    when(passwordEncoder.encode(userCreateRequestDto.passwordHash())).thenReturn("secret_password");
    when(userRepository.existsByEmail(userCreateRequestDto.email())).thenReturn(false);

    adminService.createAdmin(userCreateRequestDto);

    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepository).save(userCaptor.capture());
    UserEntity userEntity = userCaptor.getValue();

    assertEquals(userCreateRequestDto.name(), userEntity.getName());
    assertEquals(userCreateRequestDto.email(), userEntity.getEmail());
    assertEquals("secret_password", userEntity.getPasswordHash());
    assertEquals(Role.ADMIN, userEntity.getRole());
  }

  @Test
  void dontCreateAdminIfEmailExists() {

    UserCreateRequestDto userCreateRequestDto = createUserCreateRequestDto();

    when(userRepository.existsByEmail(userCreateRequestDto.email())).thenReturn(true);

    assertThrows(
        IllegalArgumentException.class, () -> adminService.createAdmin(userCreateRequestDto));

    verify(userRepository, never()).save(any(UserEntity.class));
    verify(passwordEncoder, never()).encode(anyString());
  }

  @Test
  void getAllUser() {

    UserEntity userEntity = createUserEntity();
    UserEntity userEntity1 = createUserEntity();
    UserEntity userEntity2 = createUserEntity();

    List<UserEntity> userEntities = new ArrayList<>(List.of());

    userEntities.add(userEntity);
    userEntities.add(userEntity1);
    userEntities.add(userEntity2);

    UserResponseDto expectedDto = createUserResponseDto();
    UserResponseDto expectedDto1 = createUserResponseDto();
    UserResponseDto expectedDto2 = createUserResponseDto();

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
    UserEntity userEntity = createUserEntity();

    UserResponseDto expectedDto = createUserResponseDto();

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = adminService.getUserByEmail(userEntity.getEmail());

    assertEquals(expectedDto, result);
  }

  @Test
  void deleteUser() {
    UserEntity userEntity = createUserEntity();

    UserResponseDto expectedDto = createUserResponseDto();

    when(userRepository.findByEmail(userEntity.getEmail())).thenReturn(Optional.of(userEntity));
    when(userMapper.userToDto(userEntity)).thenReturn(expectedDto);

    var result = adminService.deleteUser(userEntity.getEmail());

    verify(userRepository).delete(userEntity);
    assertEquals(expectedDto, result);
  }

  @Test
  void getDeletedApplication() {

    ApplicationEntity applicationEntity = createApplicationEntity();

    List<ApplicationEntity> applicationEntityList = List.of(applicationEntity, applicationEntity);

    List<ApplicationResponseDto> expectedApplicationList = new ArrayList<>();

    ApplicationResponseDto applicationResponseDto = createApplicationResponseDto();

    ApplicationResponseDto applicationResponseDto1 = createApplicationResponseDto();

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

    ApplicationResponseDto applicationResponseDto = createApplicationResponseDto();

    List<ActivityEventResponseDto> activityEventResponseDtoList =
        List.of(new ActivityEventResponseDto(1L, applicationResponseDto, OffsetDateTime.now()));

    when(activityRepository.findAll()).thenReturn(activityEventEntities);
    when(activityMapper.activityEventEntityToActivityResponseDto(activityEventEntities))
        .thenReturn(activityEventResponseDtoList);

    var result = adminService.getAllActivityEvent();

    assertEquals(1, result.size());
    assertEquals(activityEventResponseDtoList.getFirst(), result.getFirst());
  }

  private UserCreateRequestDto createUserCreateRequestDto(){
    return new UserCreateRequestDto(
            createUserEntity().getName(),
            createUserEntity().getEmail(),
            createUserEntity().getPasswordHash()
    );
  }
  private ApplicationEntity createApplicationEntity(){
      return new ApplicationEntity(
              1L,
              createUserEntity(),
              createVacancyEntity(),
              ApplicationStatus.DRAFT,
              OffsetDateTime.now(),
              OffsetDateTime.now(),
              1L
    );
  }

  private VacancyEntity createVacancyEntity(){
      return new VacancyEntity(
              1L,
              createVacancyResponseDto().company(),
              createVacancyResponseDto().position(),
              createVacancyResponseDto().description(),
              createUserEntity(),
              VacancyStatus.ACTIVE
      );
  }
  private UserEntity createUserEntity(){
      return new UserEntity(
              1L,
              "Jahn",
              "john@gmail.com",
              "SecretPassword",
              Role.USER,
              OffsetDateTime.now(),
              OffsetDateTime.now()
      );
  }

  private VacancyResponseDto createVacancyResponseDto(){
      return new VacancyResponseDto(
              1L,
              "Nike",
              "Junior",
              "Description"
      );
  }
  private UserResponseDto createUserResponseDto(){
      return new UserResponseDto(
              "Jahn",
              "john@gmail.com",
              Role.USER
      );
  }

  private ApplicationResponseDto createApplicationResponseDto(){
      return new ApplicationResponseDto(
              1L,
              createUserResponseDto(),
              createVacancyResponseDto(),
              ApplicationStatus.DRAFT,
              OffsetDateTime.now(),
              OffsetDateTime.now(),
              1L
      );
  }
}

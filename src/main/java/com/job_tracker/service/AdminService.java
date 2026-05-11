package com.job_tracker.service;

// import com.job_tracker.annotation.TrackExecutionTime;
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
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final UserRepository userRepository;
  private final ApplicationRepository applicationRepository;
  private final ActivityRepository activityRepository;
  private final ActivityMapper activityMapper;
  private final ApplicationMapper applicationMapper;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  //  @PreAuthorize("hasRole('ADMIN')")
  public UserResponseDto createAdmin(UserCreateRequestDto user) {

    if (userRepository.existsByEmail(user.email())) {
      throw new IllegalArgumentException("Email already exists " + user.email());
    }

    UserEntity userEntity =
        new UserEntity(
            null,
            user.name(),
            user.email(),
            passwordEncoder.encode(user.passwordHash()),
            Role.ADMIN,
            OffsetDateTime.now(),
            OffsetDateTime.now());
    userEntity = userRepository.save(userEntity);

    return userMapper.userToDto(userEntity);
  }

  public List<UserResponseDto> getAllUser() {

    List<UserEntity> userEntity = userRepository.findAll();

    return userEntity.stream().map(userMapper::userToDto).toList();
  }

  //  @TrackExecutionTime(unit = TimeUnit.SECONDS, debug = false)
  //  @PreAuthorize("hasRole('ADMIN')")
  public UserResponseDto getUserByEmail(String email) {
    UserEntity userEntity =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user by email " + email));

    return userMapper.userToDto(userEntity);
  }

  @Transactional
  //  @PreAuthorize("hasRole('ADMIN')")
  public UserResponseDto deleteUser(String email) {
    UserEntity userEntity =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user by email " + email));

    userRepository.delete(userEntity);
    return userMapper.userToDto(userEntity);
  }

  //  @PreAuthorize("hasRole('ADMIN')")
  public List<ApplicationResponseDto> getDeletedApplication() {
    List<ApplicationEntity> applicationEntity =
        applicationRepository.findByApplicationStatus(ApplicationStatus.DELETED);

    return applicationMapper.listApplicationToDto(applicationEntity);
  }

  //  @PreAuthorize("hasRole('ADMIN')")
  public List<ActivityEventResponseDto> getAllActivityEvent() {
    List<ActivityEventEntity> activityEventEntities = activityRepository.findAll();

    return activityMapper.activityEventEntityToActivityResponseDto(activityEventEntities);
  }
}

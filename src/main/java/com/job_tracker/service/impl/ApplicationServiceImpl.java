package com.job_tracker.service;

import static com.job_tracker.helper_method.SecurityUtil.getCurrentPrincipalOrThrow;

import com.job_tracker.create_exception.AccessDeniedException;
import com.job_tracker.create_exception.InvalidApplicationStatusTransition;
import com.job_tracker.dto.ApplicationCreateRequestDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.ActivityEventType;
import com.job_tracker.enums.ApplicationStatus;
import com.job_tracker.mapper.ApplicationMapper;
import com.job_tracker.repository.ActivityRepository;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private final UserRepository userRepository;
  private final ApplicationRepository applicationRepository;
  private final ActivityRepository activityRepository;
  private final ApplicationMapper applicationMapper;

  @Transactional
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public ApplicationResponseDto createApplication(ApplicationCreateRequestDto application) {

    PrincipalDto principal = getCurrentPrincipalOrThrow();

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

    if (applicationRepository.existsByUserIdAndCompanyAndPosition(
        principal.id(), application.company(), application.position())) {
      throw new IllegalArgumentException("Application already exists");
    }

    ApplicationEntity createdApplicationEntity =
        new ApplicationEntity(
            null,
            userEntity,
            application.company(),
            application.position(),
            ApplicationStatus.DRAFT,
            null,
            null,
            null);
    ApplicationEntity saved = applicationRepository.save(createdApplicationEntity);
    return applicationMapper.applicationToDto(saved);
  }

  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public List<ApplicationResponseDto> getMyApplication() {

    PrincipalDto principal = getCurrentPrincipalOrThrow();

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

    List<ApplicationEntity> userApplicationEntity =
        applicationRepository.findByUserIdAndApplicationStatusNot(
            principal.id(), ApplicationStatus.DELETED);
    return applicationMapper.listApplicationToDto(userApplicationEntity);
  }

  @Transactional
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public ApplicationResponseDto deleteMyApplicationById(Long id) {

    PrincipalDto principal = getCurrentPrincipalOrThrow();

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find application by id= " + id));

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + id));

    if (!applicationEntity.getUser().getId().equals(principal.id())) {
      throw new AccessDeniedException("You are not allowed to delete this application");
    }

    applicationEntity.setApplicationStatus(ApplicationStatus.DELETED);

    ActivityEventEntity activityEventEntity =
        new ActivityEventEntity(
            null, applicationEntity, ActivityEventType.STATUS_CHANGED, null, userEntity);

    applicationRepository.save(applicationEntity);
    activityRepository.save(activityEventEntity);

    return applicationMapper.applicationToDto(applicationEntity);
  }

  @Transactional
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  public ApplicationResponseDto updateMyApplicationStatusById(
      Long id, ApplicationStatus applicationStatus) {

    PrincipalDto principal = getCurrentPrincipalOrThrow();

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + id));

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find application by id= " + id));

    if (!applicationEntity.getApplicationStatus().canTransitionTo(applicationStatus)) {
      throw new InvalidApplicationStatusTransition(
          "Application status can't be transitioned to " + applicationStatus);
    }

    if (!applicationEntity.getUser().getId().equals(principal.id())) {
      throw new AccessDeniedException("You are not allowed to update this application");
    }

    ActivityEventEntity activityEventEntity =
        new ActivityEventEntity(
            null, applicationEntity, ActivityEventType.STATUS_CHANGED, null, userEntity);

    applicationEntity.setApplicationStatus(applicationStatus);
    applicationRepository.save(applicationEntity);
    activityRepository.save(activityEventEntity);

    return applicationMapper.applicationToDto(applicationEntity);
  }
}

package com.job_tracker.service.impl;

import com.job_tracker.create_exception.InvalidApplicationStatusTransition;
import com.job_tracker.dto.ApplicationCreateRequestDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.ActivityEventType;
import com.job_tracker.enums.ApplicationStatus;
import com.job_tracker.mapper.ApplicationMapper;
import com.job_tracker.repository.ActivityRepository;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.repository.VacancyRepository;
import com.job_tracker.service.ApplicationService;
import com.job_tracker.service.SecurityContextService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

  private final UserRepository userRepository;
  private final ApplicationRepository applicationRepository;
  private final ActivityRepository activityRepository;
  private final VacancyRepository vacancyRepository;
  private final ApplicationMapper applicationMapper;
  private final SecurityContextService securityContextService;

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @Transactional
  public ApplicationResponseDto createApplication(ApplicationCreateRequestDto application) {

    PrincipalDto principal = securityContextService.getCurrentPrincipalOrThrow();

    VacancyEntity vacancyEntity =
        vacancyRepository
            .findById(application.vacancyId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        "Cannot find vacancy by id: " + application.vacancyId()));

    UserEntity userProxy = userRepository.getReferenceById(principal.id());

    ApplicationEntity createdApplicationEntity =
        new ApplicationEntity(
            null, userProxy, vacancyEntity, ApplicationStatus.DRAFT, null, null, null);
    ApplicationEntity saved = applicationRepository.save(createdApplicationEntity);
    return applicationMapper.applicationToDto(saved);
  }

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @Transactional(readOnly = true)
  public Page<ApplicationResponseDto> getMyApplication(Pageable pageable) {
    PrincipalDto principal = securityContextService.getCurrentPrincipalOrThrow();
    return applicationRepository
        .findAllByUserId(principal.id(), pageable)
        .map(applicationMapper::applicationToDto);
  }

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @Transactional
  public ApplicationResponseDto deleteMyApplicationById(Long id) {

    PrincipalDto principal = securityContextService.getCurrentPrincipalOrThrow();

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find application by id= " + id));

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

    applicationEntity.setApplicationStatus(ApplicationStatus.DELETED);

    ActivityEventEntity activityEventEntity =
        new ActivityEventEntity(
            null, applicationEntity, ActivityEventType.STATUS_CHANGED, null, userEntity);

    applicationRepository.save(applicationEntity);
    activityRepository.save(activityEventEntity);

    return applicationMapper.applicationToDto(applicationEntity);
  }

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
  @Transactional
  public ApplicationResponseDto updateMyApplicationStatusById(Long id, ApplicationStatus status) {

    PrincipalDto principal = securityContextService.getCurrentPrincipalOrThrow();

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + id));

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Cannot find application by id= " + id));

    if (!applicationEntity.getApplicationStatus().canTransitionTo(status)) {
      throw new InvalidApplicationStatusTransition(
          "Application status can't be transitioned to " + status);
    }

    ActivityEventEntity activityEventEntity =
        new ActivityEventEntity(
            null, applicationEntity, ActivityEventType.STATUS_CHANGED, null, userEntity);

    applicationEntity.setApplicationStatus(status);
    applicationRepository.save(applicationEntity);
    activityRepository.save(activityEventEntity);

    return applicationMapper.applicationToDto(applicationEntity);
  }

  @Override
  @PreAuthorize("hasRole('ADMIN')")
  @Transactional(readOnly = true)
  public Page<ApplicationResponseDto> getDeletedApplication(Pageable pageable) {
    return applicationRepository.findAll(pageable).map(applicationMapper::applicationToDto);
  }
}

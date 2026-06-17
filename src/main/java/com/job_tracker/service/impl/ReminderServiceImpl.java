package com.job_tracker.service.impl;

import com.job_tracker.create_exception.InvalidDueAtException;
import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.dto.ReminderCreateRequestDto;
import com.job_tracker.dto.ReminderResponseDto;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.ReminderEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.ReminderStatus;
import com.job_tracker.mapper.ReminderMapper;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.ReminderRepository;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.service.ReminderService;
import com.job_tracker.service.SecurityContextService;
import jakarta.persistence.EntityNotFoundException;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReminderServiceImpl implements ReminderService {

  private final ReminderRepository reminderRepository;
  private final UserRepository userRepository;
  private final ApplicationRepository applicationRepository;
  private final ReminderMapper reminderMapper;
  private final SecurityContextService securityContextService;

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'HR')")
  @Transactional(readOnly = true)
  public Page<ReminderResponseDto> getMyReminder(Pageable pageable) {
    PrincipalDto principal = securityContextService.getCurrentPrincipalOrThrow();
    return reminderRepository
        .findAllByUserId(principal.id(), pageable)
        .map(reminderMapper::reminderToDto);
  }

  @Override
  @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'HR')")
  @Transactional
  public ReminderResponseDto createReminder(Long applicationId, ReminderCreateRequestDto reminder) {

    PrincipalDto principal = securityContextService.getCurrentPrincipalOrThrow();
    securityContextService.validateOwnershipOrThrow(principal.id());
    if (reminder.dueAt().isBefore(OffsetDateTime.now().minusSeconds(1))) {
      throw new InvalidDueAtException("Reminder date cannot be in the past: " + reminder.dueAt());
    }

    UserEntity userEntity =
        userRepository
            .findById(principal.id())
            .orElseThrow(
                () -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

    ApplicationEntity applicationEntity =
        applicationRepository
            .findById(applicationId)
            .orElseThrow(
                () ->
                    new EntityNotFoundException("Cannot find application by id= " + applicationId));

    ReminderEntity reminderEntity =
        new ReminderEntity(
            null,
            userEntity,
            applicationEntity,
            reminder.dueAt(),
            ReminderStatus.PENDING,
            reminder.message(),
            null,
            null);

    ReminderEntity savedReminder = reminderRepository.save(reminderEntity);
    return reminderMapper.reminderToDto(savedReminder);
  }
}

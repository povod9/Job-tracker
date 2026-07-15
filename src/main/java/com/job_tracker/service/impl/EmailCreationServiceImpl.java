package com.job_tracker.service.impl;

import com.job_tracker.entity.EmailEntity;
import com.job_tracker.entity.ReminderEntity;
import com.job_tracker.enums.EmailStatus;
import com.job_tracker.repository.EmailRepository;
import com.job_tracker.service.EmailCreationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailCreationServiceImpl implements EmailCreationService {

  private final EmailRepository emailRepository;

  @Transactional
  public void createEmailForReminder(ReminderEntity reminder) {
    try {
      EmailEntity emailEntity = EmailEntity.builder()
              .user(reminder.getUser())
              .reminder(reminder)
              .status(EmailStatus.PENDING)
              .build();

      emailRepository.save(emailEntity);
      log.debug(
          "Created email for reminder {} (user: {})",
          reminder.getId(),
          reminder.getUser().getEmail());
    } catch (Exception e) {
      log.error("Failed to create email for reminder {}: {}", reminder.getId(), e.getMessage());
    }
  }
}

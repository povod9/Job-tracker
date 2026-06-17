package com.job_tracker.scheduler;

import com.job_tracker.entity.EmailEntity;
import com.job_tracker.entity.ReminderEntity;
import com.job_tracker.enums.EmailStatus;
import com.job_tracker.repository.EmailRepository;
import com.job_tracker.repository.ReminderRepository;
import com.job_tracker.service.EmailCreationService;
import com.job_tracker.service.EmailService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSchedule {

  private final ReminderRepository reminderRepository;
  private final EmailRepository emailRepository;
  private final EmailCreationService createEmailForReminder;
  private final EmailService emailService;

  @Scheduled(fixedDelay = 10000)
  public void createPendingEmail() {
    try {
      List<ReminderEntity> reminderList =
          reminderRepository.findReminderWithoutEmail(OffsetDateTime.now(ZoneOffset.UTC));

      log.info("Found {} reminders without email", reminderList.size());

      reminderList.forEach(createEmailForReminder::createEmailForReminder);

    } catch (Exception e) {
      log.error("Error in email scheduler", e);
    }
  }

  @Scheduled(fixedDelay = 5000)
  public void processPendingEmail() {
    try {
      List<EmailEntity> emailList = emailRepository.findEmailByStatus(EmailStatus.PENDING);
      log.info("Processing {} pending emails", emailList.size());
      emailList.forEach(emailService::sendEmail);
    } catch (Exception e) {
      log.error("Error processing pending emails ", e);
    }
  }
}

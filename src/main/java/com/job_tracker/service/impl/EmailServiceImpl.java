package com.job_tracker.service.impl;

import com.job_tracker.entity.EmailEntity;
import com.job_tracker.enums.EmailStatus;
import com.job_tracker.enums.ReminderStatus;
import com.job_tracker.repository.EmailRepository;
import com.job_tracker.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;
  private final EmailRepository repository;

  @Override
  @Async
  public void sendEmail(EmailEntity emailEntity) {

    SimpleMailMessage message = new SimpleMailMessage();
    try {

      message.setTo(emailEntity.getUser().getEmail());
      message.setFrom("noreply@jobtrakcer.com");
      message.setSubject("Job Tracker Reminder");
      message.setText(emailEntity.getReminder().getMessage());
      mailSender.send(message);
      emailEntity.setStatus(EmailStatus.SENT);
      emailEntity.getReminder().setStatus(ReminderStatus.SENT);
      emailEntity.setErrorMessage(null);
    } catch (Exception e) {
      emailEntity.setErrorMessage(e.getMessage());
      emailEntity.setAttempts(emailEntity.getAttempts() + 1);
      if (emailEntity.getAttempts() >= 5) {
        emailEntity.setStatus(EmailStatus.FAILED);
      } else {
        emailEntity.setStatus(EmailStatus.PENDING);
      }
    } finally {
      repository.save(emailEntity);
    }
  }
}

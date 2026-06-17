package com.job_tracker.service;

import com.job_tracker.entity.EmailEntity;

public interface EmailService {

  void sendEmail(EmailEntity emailEntity);
}

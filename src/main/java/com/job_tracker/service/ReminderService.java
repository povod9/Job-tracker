package com.job_tracker.service;

import com.job_tracker.dto.ReminderCreateRequestDto;
import com.job_tracker.dto.ReminderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReminderService {
  Page<ReminderResponseDto> getMyReminder(Pageable pageable);

  ReminderResponseDto createReminder(Long applicationId, ReminderCreateRequestDto reminder);
}

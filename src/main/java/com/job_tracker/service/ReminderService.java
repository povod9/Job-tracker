package com.job_tracker.service;

import com.job_tracker.dto.ReminderCreateRequestDto;
import com.job_tracker.dto.ReminderResponseDto;
import java.util.List;

public interface ReminderService {
  List<ReminderResponseDto> getMyReminder();

  ReminderResponseDto createReminder(Long applicationId, ReminderCreateRequestDto reminder);
}

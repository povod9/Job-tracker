package com.job_tracker.service;

import com.job_tracker.dto.ActivityEventResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ActivityService {
  Page<ActivityEventResponseDto> getAllActivityEvent(Pageable pageable);
}

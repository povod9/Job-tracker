package com.job_tracker.service;

import com.job_tracker.dto.ApplicationCreateRequestDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.enums.ApplicationStatus;
import java.util.List;

public interface ApplicationService {
  ApplicationResponseDto createApplication(ApplicationCreateRequestDto application);

  List<ApplicationResponseDto> getMyApplication();

  ApplicationResponseDto deleteMyApplicationById(Long id);

  ApplicationResponseDto updateMyApplicationStatusById(
      Long id, ApplicationStatus applicationStatus);
}

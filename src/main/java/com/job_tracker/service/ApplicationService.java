package com.job_tracker.service;

import com.job_tracker.dto.ApplicationCreateRequestDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.enums.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ApplicationService {
  ApplicationResponseDto createApplication(ApplicationCreateRequestDto application);

  Page<ApplicationResponseDto> getMyApplication(Pageable pageable);

  ApplicationResponseDto deleteMyApplicationById(Long id);

  ApplicationResponseDto updateMyApplicationStatusById(Long id, ApplicationStatus status);

  Page<ApplicationResponseDto> getDeletedApplication(Pageable pageable);
}

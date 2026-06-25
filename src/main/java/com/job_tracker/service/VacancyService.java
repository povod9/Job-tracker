package com.job_tracker.service;

import com.job_tracker.dto.VacancyCreateRequestDto;
import com.job_tracker.dto.VacancyResponseDto;
import com.job_tracker.dto.VacancyUpdateDto;
import com.job_tracker.enums.VacancyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VacancyService {
  Page<VacancyResponseDto> getAllVacancy(VacancyStatus status, Pageable pageable);

  VacancyResponseDto createVacancy(VacancyCreateRequestDto vacancyCreateRequestDto);

  VacancyResponseDto getVacancyById(Long id);

  void deleteVacancy(Long id);

  VacancyResponseDto updateVacancy(Long id, VacancyUpdateDto vacancyUpdateDto);
}

package com.job_tracker.controller;

import com.job_tracker.dto.PaginationParams;
import com.job_tracker.dto.VacancyCreateRequestDto;
import com.job_tracker.dto.VacancyResponseDto;
import com.job_tracker.dto.VacancyUpdateDto;
import com.job_tracker.enums.VacancyStatus;
import com.job_tracker.service.VacancyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacancy")
@RequiredArgsConstructor
public class VacancyController {

  private final VacancyService service;


  @GetMapping("/vacancies")
  public ResponseEntity<Page<VacancyResponseDto>> getAllVacancy(
          @ModelAttribute PaginationParams params,
          @RequestParam(name = "status", required = false) VacancyStatus status) {
    Pageable pageable = PageRequest.of(params.page(), params.size(), Sort.by(params.sortBy()));
    Page<VacancyResponseDto> vacanciesResponseDto = service.getAllVacancy(status, pageable);
    return ResponseEntity.ok(vacanciesResponseDto);
  }

  @GetMapping("/me/{id}")
  public ResponseEntity<VacancyResponseDto> getVacancyById(@PathVariable("id") Long id) {
    VacancyResponseDto vacancyResponseDto = service.getVacancyById(id);
    return ResponseEntity.ok(vacancyResponseDto);
  }

  @PostMapping("/me")
  public ResponseEntity<VacancyResponseDto> createVacancy(
      @RequestBody @Valid VacancyCreateRequestDto vacancyCreateRequestDto) {
    VacancyResponseDto vacancyResponseDto = service.createVacancy(vacancyCreateRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(vacancyResponseDto);
  }

  @DeleteMapping("/me/{id}")
  public ResponseEntity<Void> deleteVacancyById(@PathVariable("id") Long id) {
    service.deleteVacancy(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("/me/{id}")
  public ResponseEntity<VacancyResponseDto> updateVacancy(
      @PathVariable("id") Long id, @RequestBody VacancyUpdateDto vacancyUpdateDto) {
    VacancyResponseDto vacancyResponseDto = service.updateVacancy(id, vacancyUpdateDto);
    return ResponseEntity.ok(vacancyResponseDto);
  }
}

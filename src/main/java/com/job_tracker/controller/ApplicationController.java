package com.job_tracker.controller;

import com.job_tracker.dto.ApplicationCreateRequestDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.dto.PaginationParams;
import com.job_tracker.enums.ApplicationStatus;
import com.job_tracker.service.ApplicationService;
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
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

  private final ApplicationService applicationService;

  @GetMapping("/me/applications")
  public ResponseEntity<Page<ApplicationResponseDto>> getMyApplication(
          @ModelAttribute PaginationParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size(), Sort.by(params.sortBy()));
    Page<ApplicationResponseDto> applicationResponseDto =
        applicationService.getMyApplication(pageable);
    return ResponseEntity.ok(applicationResponseDto);
  }

  @PostMapping("/me")
  public ResponseEntity<ApplicationResponseDto> createApplication(
      @RequestBody @Valid ApplicationCreateRequestDto application) {
    ApplicationResponseDto applicationResponseDto =
        applicationService.createApplication(application);
    return ResponseEntity.status(HttpStatus.CREATED).body(applicationResponseDto);
  }

  @PutMapping("/me/{id}")
  public ResponseEntity<ApplicationResponseDto> updateMyApplicationStatusById(
      @PathVariable("id") Long id, @RequestParam(name = "status") ApplicationStatus status) {
    ApplicationResponseDto applicationResponseDto =
        applicationService.updateMyApplicationStatusById(id, status);
    return ResponseEntity.ok(applicationResponseDto);
  }

  @DeleteMapping("/me/{id}")
  public ResponseEntity<ApplicationResponseDto> deleteMyApplicationById(
      @PathVariable("id") Long id) {
    ApplicationResponseDto deletedApplicationResponseDto =
        applicationService.deleteMyApplicationById(id);
    return ResponseEntity.ok(deletedApplicationResponseDto);
  }

  @GetMapping("/applications")
  public ResponseEntity<Page<ApplicationResponseDto>> getDeletedApplication(
          @ModelAttribute PaginationParams params) {
    Pageable pageable = PageRequest.of(params.page(), params.size(), Sort.by(params.sortBy()));
    Page<ApplicationResponseDto> applicationResponseDtoList =
        applicationService.getDeletedApplication(pageable);
    return ResponseEntity.ok(applicationResponseDtoList);
  }
}

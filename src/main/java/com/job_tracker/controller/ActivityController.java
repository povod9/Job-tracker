package com.job_tracker.controller;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ActivityController {

  private final ActivityService activityService;

  @GetMapping("/activities")
  public ResponseEntity<Page<ActivityEventResponseDto>> getAllActivityEvent(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "id") String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<ActivityEventResponseDto> activityEventResponseDto =
        activityService.getAllActivityEvent(pageable);
    return ResponseEntity.ok(activityEventResponseDto);
  }
}

package com.job_tracker.controller;

import com.job_tracker.dto.ReminderCreateRequestDto;
import com.job_tracker.dto.ReminderResponseDto;
import com.job_tracker.service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reminder")
public class ReminderController {

  private final ReminderService reminderService;

  public ReminderController(ReminderService reminderService) {
    this.reminderService = reminderService;
  }

  @PostMapping("/me/{id}")
  public ResponseEntity<ReminderResponseDto> createReminder(
      @PathVariable("id") Long applicationId,
      @RequestBody @Valid ReminderCreateRequestDto reminder) {
    ReminderResponseDto reminderResponseDto =
        reminderService.createReminder(applicationId, reminder);
    return ResponseEntity.status(HttpStatus.CREATED).body(reminderResponseDto);
  }

  @GetMapping("/me/reminders")
  public ResponseEntity<Page<ReminderResponseDto>> getMyReminder(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size,
      @RequestParam(defaultValue = "id") String sortBy) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<ReminderResponseDto> reminderResponseDto = reminderService.getMyReminder(pageable);
    return ResponseEntity.ok(reminderResponseDto);
  }
}

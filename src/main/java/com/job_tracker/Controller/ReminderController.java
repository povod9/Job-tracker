package com.job_tracker.Controller;

import com.job_tracker.Dto.ReminderCreateRequestDto;
import com.job_tracker.Dto.ReminderResponseDto;
import com.job_tracker.Service.ReminderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reminder")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping("/me/create/application/{id}")
    public ResponseEntity<ReminderResponseDto> createReminder(
            @PathVariable("id") Long applicationId,
            @RequestBody @Valid ReminderCreateRequestDto reminder
    )
    {
        ReminderResponseDto reminderResponseDto = reminderService.createReminder(applicationId,reminder);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reminderResponseDto);
    }

    @GetMapping("/me/find")
    public ResponseEntity<List<ReminderResponseDto>> getMyReminder()
    {
        List<ReminderResponseDto> reminderResponseDto = reminderService.getMyReminder();
        return ResponseEntity
                .ok(reminderResponseDto);
    }
}

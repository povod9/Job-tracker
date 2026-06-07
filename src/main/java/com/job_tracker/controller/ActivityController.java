package com.job_tracker.controller;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping("/activities")
    public ResponseEntity<List<ActivityEventResponseDto>> getAllActivityEvent() {
        List<ActivityEventResponseDto> activityEventResponseDto = activityService.getAllActivityEvent();
        return ResponseEntity.ok(activityEventResponseDto);
    }
}

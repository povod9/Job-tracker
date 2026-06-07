package com.job_tracker.service;

import com.job_tracker.dto.ActivityEventResponseDto;

import java.util.List;

public interface ActivityService {
    List<ActivityEventResponseDto> getAllActivityEvent();

}

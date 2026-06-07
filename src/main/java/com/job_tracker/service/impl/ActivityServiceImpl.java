package com.job_tracker.service.impl;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.mapper.ActivityMapper;
import com.job_tracker.repository.ActivityRepository;
import com.job_tracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public List<ActivityEventResponseDto> getAllActivityEvent() {
        List<ActivityEventEntity> activityEventEntities = activityRepository.findAll();

        return activityMapper.activityEventEntityToActivityResponseDto(activityEventEntities);
    }
}

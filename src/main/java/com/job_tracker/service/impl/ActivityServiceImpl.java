package com.job_tracker.service.impl;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.mapper.ActivityMapper;
import com.job_tracker.repository.ActivityRepository;
import com.job_tracker.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<ActivityEventResponseDto> getAllActivityEvent(Pageable pageable) {
        return activityRepository.findAll(pageable)
                .map(activityMapper::activityToDto);
    }
}

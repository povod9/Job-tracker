package com.job_tracker.service.impl;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.enums.*;
import com.job_tracker.mapper.ActivityMapper;
import com.job_tracker.repository.ActivityRepository;
import com.job_tracker.support.ObjectMotherCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

    @Mock ActivityRepository repository;
    @Mock ActivityMapper mapper;
    @InjectMocks ActivityServiceImpl service;
    ObjectMotherCreator objectMotherCreator = new ObjectMotherCreator();

    @Test
    void createActivitySuccessfully(){
        ActivityEventEntity activityEventEntity1 =  objectMotherCreator.createActivityEventEntity();
        ActivityEventEntity activityEventEntity2 =  objectMotherCreator.createActivityEventEntity();
        ActivityEventResponseDto activityEventResponseDto1 = objectMotherCreator.createActivityEventResponseDto();
        ActivityEventResponseDto activityEventResponseDto2 = objectMotherCreator.createActivityEventResponseDto();

        Page<ActivityEventEntity> page = new PageImpl<>(List.of(activityEventEntity1,activityEventEntity2));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.activityToDto(activityEventEntity1)).thenReturn(activityEventResponseDto1);
        when(mapper.activityToDto(activityEventEntity2)).thenReturn(activityEventResponseDto2);

        service.getAllActivityEvent(pageable);

        verify(repository).findAll(any(Pageable.class));
        verify(mapper, times(2)).activityToDto(any(ActivityEventEntity.class));
    }
}
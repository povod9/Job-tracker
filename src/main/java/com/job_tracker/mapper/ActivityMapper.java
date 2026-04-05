package com.job_tracker.mapper;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.entity.ActivityEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ApplicationMapper.class})
public interface ActivityMapper {

    @Mapping(source = "application", target = "applicationDto")
    ActivityEventResponseDto activityToDto(ActivityEventEntity activityEventEntity);

    List<ActivityEventResponseDto> activityEventEntityToActivityResponseDto(List<ActivityEventEntity> activityEventEntity);
}

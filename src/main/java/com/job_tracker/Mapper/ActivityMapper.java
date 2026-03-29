package com.job_tracker.Mapper;

import com.job_tracker.Dto.ActivityEventResponseDto;
import com.job_tracker.Entity.ActivityEventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ApplicationMapper.class})
public interface ActivityMapper {

    @Mapping(source = "application", target = "applicationDto")
    ActivityEventResponseDto activityToDto(ActivityEventEntity activityEventEntity);

    List<ActivityEventResponseDto> activityEventEntityToActivityResponseDto(List<ActivityEventEntity> activityEventEntity);
}

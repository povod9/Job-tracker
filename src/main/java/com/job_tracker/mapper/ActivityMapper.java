package com.job_tracker.mapper;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.entity.ActivityEventEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {ApplicationMapper.class})
public interface ActivityMapper {

  @Mapping(source = "application", target = "applicationDto")
  ActivityEventResponseDto activityToDto(ActivityEventEntity activityEventEntity);

  List<ActivityEventResponseDto> activityEventEntityToActivityResponseDto(
      List<ActivityEventEntity> activityEventEntity);
}

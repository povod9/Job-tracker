package com.job_tracker.mapper;

import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.entity.ApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(source = "user", target = "userDto")
    ApplicationResponseDto applicationToDto(ApplicationEntity applicationEntity);

    @Mapping(source = "user", target = "userDto")
    List<ApplicationResponseDto> listApplicationToDto(List<ApplicationEntity> applicationEntities);
}

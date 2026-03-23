package com.job_tracker.Mapper;

import com.job_tracker.Dto.ApplicationResponseDto;
import com.job_tracker.Dto.UserResponseDto;
import com.job_tracker.Entity.ApplicationEntity;
import com.job_tracker.Entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToDto(UserEntity userEntity);

    @Mapping(source = "user", target = "userDto")
    ApplicationResponseDto applicationToDto(ApplicationEntity applicationEntity);

    @Mapping(source = "user", target = "userDto")
    List<ApplicationResponseDto> listApplicationToDto(List<ApplicationEntity> applicationEntities);
}

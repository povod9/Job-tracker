package com.job_tracker.Mapper;

import com.job_tracker.Dto.UserResponseDto;
import com.job_tracker.Entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToDto(UserEntity userEntity);
}

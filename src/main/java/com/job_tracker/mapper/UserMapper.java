package com.job_tracker.mapper;

import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.entity.UserEntity;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponseDto userToDto(UserEntity userEntity);
}

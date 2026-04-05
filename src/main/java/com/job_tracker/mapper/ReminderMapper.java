package com.job_tracker.mapper;

import com.job_tracker.dto.ReminderResponseDto;
import com.job_tracker.entity.ReminderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

    List<ReminderResponseDto> listRemindersToDto(List<ReminderEntity> reminderEntities);
    ReminderResponseDto reminderToDto(ReminderEntity reminderEntity);

}

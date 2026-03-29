package com.job_tracker.Mapper;

import com.job_tracker.Dto.ReminderResponseDto;
import com.job_tracker.Entity.ReminderEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

    List<ReminderResponseDto> listRemindersToDto(List<ReminderEntity> reminderEntities);
    ReminderResponseDto reminderToDto(ReminderEntity reminderEntity);

}

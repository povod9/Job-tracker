package com.job_tracker.mapper;

import com.job_tracker.dto.ReminderResponseDto;
import com.job_tracker.entity.ReminderEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

  List<ReminderResponseDto> listRemindersToDto(List<ReminderEntity> reminderEntities);

  ReminderResponseDto reminderToDto(ReminderEntity reminderEntity);
}

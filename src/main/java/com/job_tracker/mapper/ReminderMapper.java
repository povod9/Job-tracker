package com.job_tracker.mapper;

import com.job_tracker.dto.ReminderResponseDto;
import com.job_tracker.entity.ReminderEntity;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReminderMapper {

  List<ReminderResponseDto> listRemindersToDto(List<ReminderEntity> reminderEntities);

  @Mapping(source = "application.id", target = "reminderApplicationResponseDto.applicationId")
  @Mapping(
      source = "application.vacancy.company",
      target = "reminderApplicationResponseDto.company")
  @Mapping(
      source = "application.vacancy.position",
      target = "reminderApplicationResponseDto.position")
  ReminderResponseDto reminderToDto(ReminderEntity reminderEntity);
}

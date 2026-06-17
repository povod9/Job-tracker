package com.job_tracker.mapper;

import com.job_tracker.dto.VacancyResponseDto;
import com.job_tracker.dto.VacancyUpdateDto;
import com.job_tracker.entity.VacancyEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

  VacancyResponseDto entityToDto(VacancyEntity vacancyEntity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateVacancyFromRequest(
      VacancyUpdateDto vacancyUpdateDto, @MappingTarget VacancyEntity vacancyEntity);
}

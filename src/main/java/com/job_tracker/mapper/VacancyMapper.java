package com.job_tracker.mapper;

import com.job_tracker.dto.VacancyResponseDto;
import com.job_tracker.entity.VacancyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VacancyMapper {

    VacancyResponseDto entityToDto(VacancyEntity vacancyEntity);
}

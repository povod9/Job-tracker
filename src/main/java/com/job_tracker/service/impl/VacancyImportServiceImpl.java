package com.job_tracker.service.impl;

import com.job_tracker.dto.AdzunaResponse;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.VacancySource;
import com.job_tracker.enums.VacancyStatus;
import com.job_tracker.repository.VacancyRepository;
import com.job_tracker.service.VacancyImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacancyImportServiceImpl implements VacancyImportService {

    private final VacancyRepository repository;


    @Override
    @Transactional
    public void saveNewVacancies(AdzunaResponse adzunaResponse){

        List<String> incomingIds = adzunaResponse.adzunaJobDtoList().stream()
                .map(AdzunaResponse.AdzunaJobDto::externalId)
                .toList();

        List<String> existingIds = repository.findAllExternalIdsByExternalIdIn(incomingIds);

        List<VacancyEntity> vacancyEntities = adzunaResponse.adzunaJobDtoList().stream()
                .filter(adzunaJob -> !existingIds.contains(adzunaJob.externalId()))
                .map(adzunaJob -> VacancyEntity.builder()
                        .externalId(adzunaJob.externalId())
                        .position(adzunaJob.position())
                        .company(adzunaJob.company().displayName())
                        .description(adzunaJob.description())
                        .salaryMax(adzunaJob.salaryMax())
                        .salaryMin(adzunaJob.salaryMin())
                        .redirectURL(adzunaJob.redirectUrl())
                        .status(VacancyStatus.ACTIVE)
                        .source(VacancySource.ADZUNA)
                        .build())
                .toList();
        log.info("Successfully imported and saved {} new vacancies from Adzuna", vacancyEntities.size());
        repository.saveAll(vacancyEntities);
    }
}

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

        List<AdzunaResponse.AdzunaJobDto> filteredDtos = adzunaResponse.adzunaJobDtoList().stream()
                .filter(job -> {

                    String title = job.position().toLowerCase();

                    boolean isSenior = title.contains("senior")
                            || title.contains("lead")
                            || title.contains("principal")
                            || title.contains("architect");

                    boolean requiredHighExperience = title.contains("3+")
                            || title.contains("mid")
                            || title.contains("regular")
                            || title.contains("experience");

                    return !isSenior && !requiredHighExperience;
                })
                .toList();
        log.info("Adzuna returned {}, jobs after filter", filteredDtos.size());

        List<String> incomingIds = filteredDtos.stream()
                .map(AdzunaResponse.AdzunaJobDto::externalId)
                .toList();

        if(filteredDtos.isEmpty()){
            return;
        }

        List<String> existingIds = repository.findAllExternalIdsByExternalIdIn(incomingIds);

        List<VacancyEntity> vacancyEntities = filteredDtos.stream()
                .filter(adzunaJob -> !existingIds.contains(adzunaJob.externalId()))
                .map(adzunaJob -> {

                    return VacancyEntity.builder()
                        .externalId(adzunaJob.externalId())
                        .position(adzunaJob.position())
                        .company(adzunaJob.company().displayName())
                        .description(adzunaJob.description())
                        .location(adzunaJob.location().area())
                        .salaryMax(adzunaJob.salaryMax())
                        .salaryMin(adzunaJob.salaryMin())
                        .redirectURL(adzunaJob.redirectUrl())
                        .status(VacancyStatus.ACTIVE)
                        .source(VacancySource.ADZUNA)
                        .build();

                })
                .toList();
        log.info("Successfully imported and saved {} new vacancies from Adzuna", vacancyEntities.size());
        repository.saveAll(vacancyEntities);
    }
}

package com.job_tracker.scheduler;

import com.job_tracker.dto.AdzunaResponse;
import com.job_tracker.entity.AppSettingsEntity;
import com.job_tracker.integration.AdzunaClient;
import com.job_tracker.repository.AppSettingsRepository;
import com.job_tracker.service.VacancyImportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class AdzunaSchedule {

    private final VacancyImportService service;
    private final AppSettingsRepository repository;
    private final AdzunaClient client;

    @Scheduled(cron = "0 * * * * *")
    public void runImport(){
        try {
            AppSettingsEntity settings = repository.findById("adzuna_current_page")
                    .orElseGet(() -> AppSettingsEntity.builder()
                            .appKey("adzuna_current_page")
                            .appValue("1")
                            .build());
            int currentPage = Integer.parseInt(settings.getAppValue());

            AdzunaResponse adzunaResponse = client.vacancyList(currentPage);
            service.saveNewVacancies(adzunaResponse);

            int nextPage = currentPage + 1;
            if(nextPage > 50) {
                nextPage = 1;
            }

            settings.setAppValue(String.valueOf(nextPage));
            repository.save(settings);

            log.info("Adzuna page updated to: {} ", nextPage);
        } catch (Exception e) {
            log.error("Error in adzuna schedule", e);
        }
    }
}

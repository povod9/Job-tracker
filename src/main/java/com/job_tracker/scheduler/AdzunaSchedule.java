package com.job_tracker.scheduler;

import com.job_tracker.dto.AdzunaResponse;
import com.job_tracker.integration.AdzunaClient;
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
    private final AdzunaClient client;

    @Scheduled(cron = "0 * * * * *")
    public void runImport(){
        try {
            AdzunaResponse adzunaResponse = client.vacancyList();
            service.saveNewVacancies(adzunaResponse);
        } catch (Exception e) {
            log.error("Error in adzuna schedule");
        }
    }
}

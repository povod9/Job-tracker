package com.job_tracker.service;

import com.job_tracker.dto.AdzunaResponse;

public interface VacancyImportService {

    void saveNewVacancies(AdzunaResponse adzunaResponse);
}

package com.job_tracker.service.impl;

import com.job_tracker.integration.AdzunaClient;
import com.job_tracker.repository.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VacancyImportService {

    private final AdzunaClient client;
    private final VacancyRepository repository;


}

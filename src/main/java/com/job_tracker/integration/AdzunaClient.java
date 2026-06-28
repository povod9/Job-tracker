package com.job_tracker.integration;

import com.job_tracker.dto.AdzunaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AdzunaClient {

    private final RestClient client;
    private final String apiKey;
    private final String apiId;

    public AdzunaClient(
            @Value("${adzuna.url}") String baseUrl,
            @Value("${adzuna.api-key}") String apiKey,
            @Value("${adzuna.api-id}") String apiId) {
        this.client = RestClient.builder().baseUrl(baseUrl).build();
        this.apiId = apiId;
        this.apiKey = apiKey;
    }

    public AdzunaResponse vacancyList(int page){
        return client.get()
                .uri(u -> u
                        .path("/jobs/pl/search/{page}")
                        .queryParam("app_id", apiId)
                        .queryParam("app_key", apiKey)
                        .queryParam("results_per_page", 10)
                        .queryParam("what", "java")
                        .queryParam("content-type", "application/json")
                        .build(page))
                .retrieve()
                .body(AdzunaResponse.class);
    }
}

package com.job_tracker.integration;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record AdzunaResponse(
        List<AdzunaJobDto> adzunaJobDtoList,
        int count
) {
    public record AdzunaJobDto(
            String adzuraId,
            @JsonProperty("title") String position,
            String description,
            @JsonProperty("redirect_url") String redirectUrl,
            AdzunaCompanyDto company,
            @JsonProperty("salary_max") BigDecimal salaryMax,
            @JsonProperty("salary_min") BigDecimal salaryMin
            ){}

    public record AdzunaCompanyDto(
      @JsonProperty("display_name") String displayName
    ){}
}

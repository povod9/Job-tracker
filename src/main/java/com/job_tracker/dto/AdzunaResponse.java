package com.job_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record AdzunaResponse(
        @JsonProperty("results") List<AdzunaJobDto> adzunaJobDtoList,
        int count
) {
    public record AdzunaJobDto(
            String externalId,
            @JsonProperty("title") String position,
            AdzunaCompanyDto company,
            String description,
            @JsonProperty("redirect_url") String redirectUrl,
            @JsonProperty("salary_max") BigDecimal salaryMax,
            @JsonProperty("salary_min") BigDecimal salaryMin
            ){}

    public record AdzunaCompanyDto(
      @JsonProperty("display_name") String displayName
    ){}
}

package com.job_tracker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public record AdzunaResponse(
        @JsonProperty("results") List<AdzunaJobDto> adzunaJobDtoList,
        int count
) {
    public record AdzunaJobDto(
            @JsonProperty("id") String externalId,
            @JsonProperty("title") String position,
            AdzunaCompanyDto company,
            String description,
            AdzunaCompanyLocation location,
            @JsonProperty("redirect_url") String redirectUrl,
            @JsonProperty("salary_max") BigDecimal salaryMax,
            @JsonProperty("salary_min") BigDecimal salaryMin
            ){}

    public record AdzunaCompanyDto(
      @JsonProperty("display_name") String displayName
    ){}

    public record AdzunaCompanyLocation(
            @JsonProperty("area") List<String> area
    ){}
}

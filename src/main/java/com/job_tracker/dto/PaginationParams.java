package com.job_tracker.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record PaginationParams(
        @Min(value = 0,message = "Page cannot be less than 0")
        int page,

        @Max(value = 20, message = "Size cannot be bigger than 20")
        int size,

        String sortBy
){
    public PaginationParams{
        if(size <= 0) size = 10;
        if(sortBy == null) sortBy = "id";
    }
}

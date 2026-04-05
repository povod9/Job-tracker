package com.job_tracker.controller;

import com.job_tracker.dto.ApplicationCreateRequestDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.enums.ApplicationStatus;
import com.job_tracker.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }


    @GetMapping("/me/application")
    public ResponseEntity<List<ApplicationResponseDto>> getMyApplication()
    {
        List<ApplicationResponseDto> applicationResponseDto = applicationService.getMyApplication();
        return ResponseEntity
                .ok(applicationResponseDto);
    }

    @PostMapping("/me/create")
    public ResponseEntity<ApplicationResponseDto> createApplication(
            @RequestBody @Valid ApplicationCreateRequestDto application
    )
    {
        ApplicationResponseDto applicationResponseDto = applicationService.createApplication(application);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(applicationResponseDto);
    }

    @PutMapping("/me/update/status/{id}")
    public ResponseEntity<ApplicationResponseDto> updateMyApplicationStatusById(
            @PathVariable("id") Long id,
            @RequestParam ApplicationStatus applicationStatus
    )
    {
        ApplicationResponseDto applicationResponseDto = applicationService.updateMyApplicationStatusById(id, applicationStatus);
        return  ResponseEntity
                .ok(applicationResponseDto);
    }

    @DeleteMapping("/me/delete/{id}")
    public ResponseEntity<ApplicationResponseDto> deleteMyApplicationById(
            @PathVariable("id") Long id
    )
    {
        ApplicationResponseDto deletedApplicationResponseDto = applicationService.deleteMyApplicationById(id);
        return ResponseEntity
                .ok(deletedApplicationResponseDto);
    }
}

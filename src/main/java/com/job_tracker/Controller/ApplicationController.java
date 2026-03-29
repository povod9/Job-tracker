package com.job_tracker.Controller;

import com.job_tracker.Dto.ApplicationCreateRequestDto;
import com.job_tracker.Dto.ApplicationResponseDto;
import com.job_tracker.Enums.ApplicationStatus;
import com.job_tracker.Service.ApplicationService;
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


    @GetMapping("/me/find/application")
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

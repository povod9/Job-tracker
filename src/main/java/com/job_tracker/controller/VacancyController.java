package com.job_tracker.controller;

import com.job_tracker.dto.VacancyCreateRequestDto;
import com.job_tracker.dto.VacancyResponseDto;
import com.job_tracker.enums.VacancyStatus;
import com.job_tracker.service.VacancyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vacancy")
public class VacancyController {

    private final VacancyService service;

    public VacancyController(VacancyService service) {
        this.service = service;
    }

    @GetMapping("/me/vacancies")
    public ResponseEntity<List<VacancyResponseDto>> getAllMyVacancy(
            @RequestParam(name = "status", required = false) VacancyStatus status){
        List<VacancyResponseDto> vacanciesResponseDto = service.getAllMyVacancy(status);
        return ResponseEntity.ok(vacanciesResponseDto);
    }

    @GetMapping("/me/{id}")
    public ResponseEntity<VacancyResponseDto> getVacancyById(@PathVariable("id") Long id){
        VacancyResponseDto vacancyResponseDto = service.getVacancyById(id);
        return ResponseEntity.ok(vacancyResponseDto);
    }

    @PostMapping("/me")
    public ResponseEntity<VacancyResponseDto> createVacancy(
            @RequestBody @Valid VacancyCreateRequestDto vacancyCreateRequestDto){
        VacancyResponseDto vacancyResponseDto = service.createVacancy(vacancyCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(vacancyResponseDto);
    }

    @DeleteMapping("/me/{id}")
    public ResponseEntity<Void> deleteVacancyById(@PathVariable("id") Long id){
        service.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }
}

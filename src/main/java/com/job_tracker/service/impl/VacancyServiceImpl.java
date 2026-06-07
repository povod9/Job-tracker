package com.job_tracker.service.impl;

import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.dto.VacancyCreateRequestDto;
import com.job_tracker.dto.VacancyResponseDto;
import com.job_tracker.dto.VacancyUpdateDto;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.VacancyStatus;
import com.job_tracker.mapper.VacancyMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.repository.VacancyRepository;
import com.job_tracker.service.SecurityContextService;
import com.job_tracker.service.VacancyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacancyServiceImpl implements VacancyService {

    private final VacancyRepository vacancyRepository;
    private final UserRepository userRepository;
    private final VacancyMapper mapper;
    private final SecurityContextService securityContextService;

    @Override
    @PreAuthorize("hasRole('HR')")
    @Transactional(readOnly = true)
    public List<VacancyResponseDto> getAllMyVacancy(VacancyStatus status) {
        PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();
        List<VacancyEntity> vacancies;
        if(status != null){
            vacancies = vacancyRepository.findAllByUserIdAndStatus(principalDto.id(), status);
        }else {
            vacancies = vacancyRepository.findAllByUserIdAndStatusNot(principalDto.id(), VacancyStatus.DELETED);
        }
        return vacancies.stream()
                .map(mapper::entityToDto)
                .toList();
    }

    @Override
    @PreAuthorize("hasRole('HR')")
    @Transactional
    public VacancyResponseDto createVacancy(VacancyCreateRequestDto vacancyCreateRequestDto) {
        PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();
        UserEntity proxyEntity = userRepository.getReferenceById(principalDto.id());
        VacancyEntity createdVacancyEntity = new VacancyEntity(
                null,
                vacancyCreateRequestDto.company(),
                vacancyCreateRequestDto.position(),
                vacancyCreateRequestDto.description(),
                proxyEntity,
                VacancyStatus.ACTIVE,
                null,
                null,
                null
        );


        vacancyRepository.save(createdVacancyEntity);
        return mapper.entityToDto(createdVacancyEntity);
    }

    @Override
    @PreAuthorize("hasRole('HR')")
    @Transactional(readOnly = true)
    public VacancyResponseDto getVacancyById(Long id) {
        PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();
        VacancyEntity vacancyEntity = vacancyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find vacancy by id: " + id));
        securityContextService.validateOwnershipOrThrow(vacancyEntity.getUser().getId());
        return mapper.entityToDto(vacancyEntity);
    }

    @Override
    @PreAuthorize("hasRole('HR')")
    @Transactional
    public void deleteVacancy(Long id) {
        PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();
        VacancyEntity vacancyEntity = vacancyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find vacancy by id: " + id));
        securityContextService.validateOwnershipOrThrow(vacancyEntity.getUser().getId());
        vacancyEntity.setStatus(VacancyStatus.DELETED);
    }

    @Override
    @PreAuthorize("hasRole('HR')")
    @Transactional
    public VacancyResponseDto updateVacancy(Long id, VacancyUpdateDto vacancyUpdateDto) {
        PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();
        VacancyEntity vacancyEntity = vacancyRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Cannot find vacancy by id: " + id));

        mapper.updateVacancyFromRequest(vacancyUpdateDto, vacancyEntity);
        return mapper.entityToDto(vacancyEntity);
    }
}

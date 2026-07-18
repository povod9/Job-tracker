package com.job_tracker.service.impl;

import com.job_tracker.create_exception.InvalidCredentialsException;
import com.job_tracker.dto.*;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.*;
import com.job_tracker.mapper.ApplicationMapper;
import com.job_tracker.repository.ActivityRepository;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.repository.VacancyRepository;
import com.job_tracker.service.SecurityContextService;
import com.job_tracker.support.ObjectMotherCreator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    UserRepository userRepository;
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    ActivityRepository activityRepository;
    @Mock
    VacancyRepository vacancyRepository;
    @Mock
    ApplicationMapper applicationMapper;
    @Mock
    SecurityContextService securityContextService;

    @InjectMocks
    ApplicationServiceImpl service;

    ObjectMotherCreator objectMotherCreator = new ObjectMotherCreator();

    @Test
    void createApplicationSuccessfully(){
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        UserEntity userEntity = objectMotherCreator.createUserEntity();
        VacancyEntity vacancyEntity = objectMotherCreator.createVacancyEntityWithStatusActive();
        ApplicationCreateRequestDto appRequest = objectMotherCreator.createApplicationCreateRequest();
        ApplicationEntity applicationEntity = objectMotherCreator.createApplicationEntity();
        ApplicationResponseDto appResponse = objectMotherCreator.createApplicationResponse();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(userRepository.getReferenceById(principalDto.id())).thenReturn(userEntity);
        when(vacancyRepository.findById(appRequest.vacancyId())).thenReturn(Optional.of(vacancyEntity));
        when(applicationRepository.save(any(ApplicationEntity.class))).thenReturn(applicationEntity);
        when(applicationMapper.applicationToDto(applicationEntity)).thenReturn(appResponse);

        var actual = service.createApplication(appRequest);

        assertNotNull(actual);
        assertEquals(appResponse, actual);

        ArgumentCaptor<ApplicationEntity> captor = ArgumentCaptor.forClass(ApplicationEntity.class);
        verify(applicationRepository).save(captor.capture());
        ApplicationEntity appValue = captor.getValue();

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(userRepository).getReferenceById(any());
        verify(vacancyRepository).findById(any());
        verify(applicationMapper).applicationToDto(any());


        assertEquals(userEntity, appValue.getUser());
        assertEquals(vacancyEntity, appValue.getVacancy());
        assertEquals(ApplicationStatus.DRAFT, appValue.getApplicationStatus());
    }

    @Test
    void doNotCreateIfVacancyIsNotExists(){
        ApplicationCreateRequestDto appRequest = objectMotherCreator.createApplicationCreateRequest();
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(vacancyRepository.findById(appRequest.vacancyId())).thenReturn(Optional.empty());

        var actualMessage = assertThrows(EntityNotFoundException.class,
                () -> service.createApplication(appRequest));

        verify(applicationRepository, never()).save(any());
        verify(applicationMapper, never()).applicationToDto(any());
        assertEquals("Cannot find vacancy by id: " + appRequest.vacancyId(), actualMessage.getMessage());
    }

    @Test
    void returnUsersApplications(){
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        ApplicationEntity applicationEntity1 = objectMotherCreator.createApplicationEntity();
        ApplicationEntity applicationEntity2 = objectMotherCreator.createApplicationEntity();
        ApplicationResponseDto applicationResponseDto1 = objectMotherCreator.createApplicationResponse();
        ApplicationResponseDto applicationResponseDto2 = objectMotherCreator.createApplicationResponse();

        Page<ApplicationEntity> appList = new PageImpl<>(List.of(applicationEntity1, applicationEntity2));
        Page<ApplicationResponseDto> expectedResponse = new PageImpl<>(List.of(applicationResponseDto1, applicationResponseDto2));
        Pageable pageable = PageRequest.of(0, 10);

        when(applicationRepository.findAllByUserId(principalDto.id(), pageable)).thenReturn(appList);
        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(applicationMapper.applicationToDto(applicationEntity1)).thenReturn(applicationResponseDto1);
        when(applicationMapper.applicationToDto(applicationEntity2)).thenReturn(applicationResponseDto2);

        var actual = service.getMyApplication(pageable);

        verify(applicationRepository).findAllByUserId(any(), any());
        verify(applicationMapper, times(2)).applicationToDto(any());

        assertEquals(expectedResponse.getSize(), actual.getSize());
        assertEquals(expectedResponse.getTotalElements(), actual.getTotalElements());
        assertEquals(expectedResponse.getTotalPages(), actual.getTotalPages());
        assertEquals(expectedResponse.getContent(), actual.getContent());
    }

    @Test
    void doNotReturnApplicationsIfUserIsNotLogin(){
        Pageable pageable = PageRequest.of(0, 10);
        when(securityContextService.getCurrentPrincipalOrThrow()).thenThrow(new InvalidCredentialsException("You are unauthorized"));

        var actual = assertThrows(InvalidCredentialsException.class,
                () -> service.getMyApplication(pageable));

        verify(applicationRepository, never()).findAllByUserId(any(),any());
        assertEquals("You are unauthorized", actual.getMessage());
    }

    @Test
    void deleteApplicationSuccessfully(){
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        UserEntity userEntity = objectMotherCreator.createUserEntity();
        VacancyEntity vacancyEntity = objectMotherCreator.createVacancyEntityWithStatusActive();
        ApplicationCreateRequestDto appRequest = objectMotherCreator.createApplicationCreateRequest();
        ApplicationEntity applicationEntity = objectMotherCreator.createApplicationEntity();
        ApplicationResponseDto appResponse = objectMotherCreator.createApplicationResponse();
        ActivityEventEntity activityEventEntity = objectMotherCreator.createActivityEventEntity();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(applicationEntity));
        when(applicationRepository.save(any(ApplicationEntity.class))).thenReturn(applicationEntity);
        when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));
        when(activityRepository.save(any(ActivityEventEntity.class))).thenReturn(activityEventEntity);
        when(applicationMapper.applicationToDto(applicationEntity)).thenReturn(appResponse);

        var actual = service.deleteMyApplicationById(1L);

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(applicationRepository).findById(anyLong());
        verify(applicationRepository).save(any(ApplicationEntity.class));
        verify(userRepository).findById(anyLong());
        verify(activityRepository).save(any());
        verify(applicationMapper).applicationToDto(any(ApplicationEntity.class));

        assertEquals(appResponse.applicationStatus(), actual.applicationStatus());
        assertEquals(appResponse.vacancyDto(), actual.vacancyDto());
        assertEquals(appResponse.id(), actual.id());
    }
}
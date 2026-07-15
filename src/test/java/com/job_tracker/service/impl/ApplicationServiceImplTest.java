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
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.action.internal.EntityActionVetoException;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
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

    @Test
    void createApplicationSuccessfully(){
        PrincipalDto principalDto = createPrincipal();
        UserEntity userEntity = createUserEntity();
        VacancyEntity vacancyEntity = createVacancyEntity();
        ApplicationCreateRequestDto appRequest = createApplicationCreateRequest();
        ApplicationEntity applicationEntity = createApplicationEntity();
        ApplicationResponseDto appResponse = createApplicationResponse();

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
        ApplicationCreateRequestDto appRequest = createApplicationCreateRequest();
        PrincipalDto principalDto = createPrincipal();
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
        PrincipalDto principalDto = createPrincipal();
        ApplicationEntity applicationEntity1 = createApplicationEntity();
        ApplicationEntity applicationEntity2 = createApplicationEntity();
        ApplicationResponseDto applicationResponseDto1 = createApplicationResponse();
        ApplicationResponseDto applicationResponseDto2 = createApplicationResponse();

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
        PrincipalDto principalDto = createPrincipal();
        UserEntity userEntity = createUserEntity();
        VacancyEntity vacancyEntity = createVacancyEntity();
        ApplicationCreateRequestDto appRequest = createApplicationCreateRequest();
        ApplicationEntity applicationEntity = createApplicationEntity();
        ApplicationResponseDto appResponse = createApplicationResponse();
        ActivityEventEntity activityEventEntity = createActivityEventEntity();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(applicationEntity));
        when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));
        when(activityRepository.save(any(ActivityEventEntity.class))).thenReturn(activityEventEntity);

        var actual = service.deleteMyApplicationById(1L);

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(applicationRepository).save(any());
        verify(userRepository).save(any());
        verify(activityRepository).save(any());

        assertEquals(appResponse.applicationStatus(), actual.applicationStatus());
        assertEquals(appResponse.vacancyDto(), actual.vacancyDto());
        assertEquals(appResponse.id(), actual.id());
    }

    private ActivityEventEntity createActivityEventEntity(){
        return new ActivityEventEntity(
                1L,
                createApplicationEntity(),
                ActivityEventType.STATUS_CHANGED,
                OffsetDateTime.now(),
                createUserEntity()
        );
    }

    private UserEntity createUserEntity() {
        return new UserEntity(
                1L,
                "Jahn",
                "jahn@gmail.com",
                "SecretPassword",
                Role.USER,
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    private PrincipalDto createPrincipal(){
        return new PrincipalDto("jahn@gmail.com", "USER", 1L);
    }

    private VacancyEntity createVacancyEntity(){
        return new VacancyEntity(
                1L,
                "123",
                "Nike",
                "Cashier",
                "description",
                List.of("Krakow"),
                createUserEntity(),
                VacancyStatus.ACTIVE,
                VacancySource.MANUAL,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                1L,
                BigDecimal.valueOf(5000.00),
                BigDecimal.valueOf(2000.00),
                "URL"
        );
    }

    private ApplicationCreateRequestDto createApplicationCreateRequest() {
        return new ApplicationCreateRequestDto(1L);
    }

    private ApplicationEntity createApplicationEntity(){
        return new ApplicationEntity(
                1L,
                createUserEntity(),
                createVacancyEntity(),
                ApplicationStatus.DRAFT,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                1L
        );
    }

    private UserResponseDto createUserResponseDto() {
        return new UserResponseDto("Jahn", "jahn@gmail.com", Role.USER);
    }

    private ApplicationResponseDto createApplicationResponse(){
        return new ApplicationResponseDto(
                1L,
                createUserResponseDto(),
                createVacancyResponseDto(),
                createApplicationEntity().getApplicationStatus(),
                createApplicationEntity().getCreatedAt(),
                createApplicationEntity().getUpdatedAt(),
                createApplicationEntity().getVersion()
        );
    }

    private VacancyResponseDto createVacancyResponseDto(){
        return new VacancyResponseDto(
                1L,
                createVacancyEntity().getCompany(),
                createVacancyEntity().getPosition(),
                createVacancyEntity().getDescription(),
                createVacancyEntity().getStatus(),
                createVacancyEntity().getSource(),
                createVacancyEntity().getSalaryMax(),
                createVacancyEntity().getSalaryMin(),
                createVacancyEntity().getLocation(),
                createVacancyEntity().getRedirectURL()
        );
    }
}
package com.job_tracker.service.impl;

import com.job_tracker.create_exception.InvalidCredentialsException;
import com.job_tracker.dto.*;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.enums.VacancySource;
import com.job_tracker.enums.VacancyStatus;
import com.job_tracker.mapper.VacancyMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.repository.VacancyRepository;
import com.job_tracker.service.SecurityContextService;
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
class VacancyServiceImplTest {

    @Mock VacancyRepository vacancyRepository;
    @Mock UserRepository userRepository;
    @Mock VacancyMapper mapper;
    @Mock SecurityContextService securityContextService;
    @InjectMocks VacancyServiceImpl service;

    @Test
    void createVacancySuccessfully(){
        PrincipalDto principalDto = createPrincipal();
        UserEntity userEntity = createUserEntity();
        VacancyCreateRequestDto vacancyRequest = createVacancyRequestDto();
        VacancyResponseDto vacancyResponse = createVacancyResponseDtoWithStatusActive();
        VacancyEntity vacancyEntity = createVacancyEntityWithStatusActive();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(userRepository.getReferenceById(principalDto.id())).thenReturn(userEntity);
        when(vacancyRepository.save(any(VacancyEntity.class))).thenReturn(vacancyEntity);
        when(mapper.entityToDto(any(VacancyEntity.class))).thenReturn(vacancyResponse);

        var actual = service.createVacancy(vacancyRequest);

        ArgumentCaptor<VacancyEntity> captor = ArgumentCaptor.forClass(VacancyEntity.class);
        verify(vacancyRepository).save(captor.capture());
        VacancyEntity vacancyValue = captor.getValue();

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(userRepository).getReferenceById(principalDto.id());
        verify(mapper).entityToDto(any(VacancyEntity.class));


        //Pre-save
        assertSame(userEntity, vacancyValue.getUser());
        assertEquals(VacancySource.MANUAL, vacancyValue.getSource());
        assertEquals(VacancyStatus.ACTIVE, vacancyValue.getStatus());
        assertEquals(vacancyRequest.company(), vacancyValue.getCompany());
        assertEquals(vacancyRequest.position(), vacancyValue.getPosition());
        assertEquals(vacancyRequest.description(), vacancyValue.getDescription());
        assertEquals(vacancyRequest.location(), vacancyValue.getLocation());
        assertEquals(vacancyRequest.salaryMax(), vacancyValue.getSalaryMax());
        assertEquals(vacancyRequest.salaryMin(), vacancyValue.getSalaryMin());

        //Service result
        assertEquals(vacancyResponse, actual);
    }

    @Test
    void listAllVacancyWithStatusActiveSuccessfully(){
        VacancyEntity vacancyEntity1 = createVacancyEntityWithStatusActive();
        VacancyEntity vacancyEntity2 = createVacancyEntityWithStatusActive();
        VacancyResponseDto vacancyResponse1 = createVacancyResponseDtoWithStatusActive();
        VacancyResponseDto vacancyResponse2 = createVacancyResponseDtoWithStatusActive();
        PrincipalDto principalDto = createPrincipal();

        Pageable pageable = PageRequest.of(0,10);
        Page<VacancyEntity> page = new PageImpl<>(List.of(vacancyEntity1,vacancyEntity2));
        Page<VacancyResponseDto> expectedPage = new PageImpl<>(List.of(vacancyResponse1,vacancyResponse2));

        VacancyStatus status = VacancyStatus.ACTIVE;

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(vacancyRepository.findAllByStatus(status,pageable)).thenReturn(page);
        when(mapper.entityToDto(vacancyEntity1)).thenReturn(vacancyResponse1);
        when(mapper.entityToDto(vacancyEntity2)).thenReturn(vacancyResponse2);

        var actual = service.getAllVacancy(status,pageable);

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(vacancyRepository).findAllByStatus(any(VacancyStatus.class),any(Pageable.class));
        verify(mapper, times(2)).entityToDto(any(VacancyEntity.class));

        assertEquals(expectedPage.getSize(), actual.getSize());
        assertEquals(expectedPage.getContent(), actual.getContent());
    }

    @Test
    void listAllVacancyWithStatusDeletedSuccessfully(){
        VacancyEntity vacancyEntity1 = createVacancyEntityWithStatusDeleted();
        VacancyEntity vacancyEntity2 = createVacancyEntityWithStatusDeleted();
        VacancyStatus status = VacancyStatus.DELETED;

        VacancyResponseDto vacancyResponse1 = createVacancyResponseDtoWithStatusDeleted();
        VacancyResponseDto vacancyResponse2 = createVacancyResponseDtoWithStatusDeleted();
        PrincipalDto principalDto = createPrincipal();

        Pageable pageable = PageRequest.of(0,10);
        Page<VacancyEntity> page = new PageImpl<>(List.of(vacancyEntity1,vacancyEntity2));
        Page<VacancyResponseDto> expectedPage = new PageImpl<>(List.of(vacancyResponse1,vacancyResponse2));

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(vacancyRepository.findAllByStatus(status,pageable)).thenReturn(page);
        when(mapper.entityToDto(vacancyEntity1)).thenReturn(vacancyResponse1);
        when(mapper.entityToDto(vacancyEntity2)).thenReturn(vacancyResponse2);

        var actual = service.getAllVacancy(status,pageable);

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(vacancyRepository).findAllByStatus(any(VacancyStatus.class), any(Pageable.class));
        verify(mapper, times(2)).entityToDto(any(VacancyEntity.class));

        assertEquals(expectedPage.getSize(), actual.getSize());
        assertEquals(expectedPage.getTotalPages(), actual.getTotalPages());
        assertEquals(expectedPage.getContent(), actual.getContent());
    }

    @Test
    void findVacancySuccessfullyById(){
        PrincipalDto principalDto = createPrincipal();
        VacancyEntity vacancyEntity = createVacancyEntityWithStatusActive();
        VacancyResponseDto vacancyResponse = createVacancyResponseDtoWithStatusActive();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        doNothing().when(securityContextService).validateOwnershipOrThrow(vacancyEntity.getUser().getId());
        when(vacancyRepository.findById(1L)).thenReturn(Optional.of(vacancyEntity));
        when(mapper.entityToDto(vacancyEntity)).thenReturn(vacancyResponse);

        var actual = service.getVacancyById(1L);

        verify(securityContextService).validateOwnershipOrThrow(anyLong());
        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(vacancyRepository).findById(anyLong());

        assertSame(vacancyResponse, actual);
    }

    @Test
    void deleteVacancySuccessfully(){
        PrincipalDto principalDto = createPrincipal();
        VacancyEntity vacancyEntity = createVacancyEntityWithStatusActive();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        doNothing().when(securityContextService).validateOwnershipOrThrow(vacancyEntity.getUser().getId());
        when(vacancyRepository.findById(1L)).thenReturn(Optional.of(vacancyEntity));

        service.deleteVacancy(1L);

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(securityContextService).validateOwnershipOrThrow(anyLong());
        verify(vacancyRepository).findById(anyLong());

        assertEquals(VacancyStatus.DELETED, vacancyEntity.getStatus());
    }

    @Test
    void updateVacancySuccessfully(){
        VacancyUpdateDto vacancyUpdateDto = createVacancyUpdateDto();
        VacancyEntity vacancyEntity = createVacancyEntityWithStatusActive();
        PrincipalDto principalDto = createPrincipal();
        VacancyResponseDto expectedResponse = updatedVacancyEntityResponse();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(vacancyRepository.findById(1L)).thenReturn(Optional.of(vacancyEntity));
        doNothing().when(mapper).updateVacancyFromRequest(vacancyUpdateDto,vacancyEntity);
        when(mapper.entityToDto(vacancyEntity)).thenReturn(expectedResponse);

        var actual = service.updateVacancy(1L,vacancyUpdateDto);

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(vacancyRepository).findById(anyLong());
        verify(mapper).updateVacancyFromRequest(any(VacancyUpdateDto.class), any(VacancyEntity.class));
        verify(mapper).entityToDto(any(VacancyEntity.class));

        assertSame(expectedResponse, actual);
    }


    private VacancyUpdateDto createVacancyUpdateDto(){
        return new VacancyUpdateDto(
                "pBank",
                "Manager",
                "description"
        );
    }

    private VacancyResponseDto updatedVacancyEntityResponse(){
        return new VacancyResponseDto(
                1L,
                "pBank",
                "Manager",
                "description",
                createVacancyEntityWithStatusActive().getStatus(),
                createVacancyEntityWithStatusActive().getSource(),
                createVacancyEntityWithStatusActive().getSalaryMax(),
                createVacancyEntityWithStatusActive().getSalaryMin(),
                createVacancyEntityWithStatusActive().getLocation(),
                createVacancyEntityWithStatusActive().getRedirectURL()
        );
    }

    private VacancyEntity createVacancyEntityWithStatusActive(){
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



    private VacancyResponseDto createVacancyResponseDtoWithStatusActive(){
        return new VacancyResponseDto(
                1L,
                createVacancyEntityWithStatusActive().getCompany(),
                createVacancyEntityWithStatusActive().getPosition(),
                createVacancyEntityWithStatusActive().getDescription(),
                createVacancyEntityWithStatusActive().getStatus(),
                createVacancyEntityWithStatusActive().getSource(),
                createVacancyEntityWithStatusActive().getSalaryMax(),
                createVacancyEntityWithStatusActive().getSalaryMin(),
                createVacancyEntityWithStatusActive().getLocation(),
                createVacancyEntityWithStatusActive().getRedirectURL()
        );
    }

    private VacancyResponseDto createVacancyResponseDtoWithStatusDeleted(){
        return new VacancyResponseDto(
                1L,
                createVacancyEntityWithStatusDeleted().getCompany(),
                createVacancyEntityWithStatusDeleted().getPosition(),
                createVacancyEntityWithStatusDeleted().getDescription(),
                createVacancyEntityWithStatusDeleted().getStatus(),
                createVacancyEntityWithStatusDeleted().getSource(),
                createVacancyEntityWithStatusDeleted().getSalaryMax(),
                createVacancyEntityWithStatusDeleted().getSalaryMin(),
                createVacancyEntityWithStatusDeleted().getLocation(),
                createVacancyEntityWithStatusDeleted().getRedirectURL()
        );
    }

    private VacancyEntity createVacancyEntityWithStatusDeleted(){
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


    private VacancyCreateRequestDto createVacancyRequestDto(){
        return new VacancyCreateRequestDto(
                createVacancyEntityWithStatusActive().getCompany(),
                createVacancyEntityWithStatusActive().getPosition(),
                createVacancyEntityWithStatusActive().getDescription(),
                createVacancyEntityWithStatusActive().getSalaryMax(),
                createVacancyEntityWithStatusActive().getSalaryMin(),
                createVacancyEntityWithStatusActive().getLocation()
        );
    }

    private UserEntity createUserEntity() {
        return new UserEntity(
                1L,
                "Jahn",
                "jahn@gmail.com",
                "SecretPassword",
                Role.HR,
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    private PrincipalDto createPrincipal(){
        return new PrincipalDto("jahn@gmail.com", "USER", 1L);
    }

}
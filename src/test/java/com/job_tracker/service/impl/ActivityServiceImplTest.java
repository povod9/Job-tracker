package com.job_tracker.service.impl;

import com.job_tracker.dto.ActivityEventResponseDto;
import com.job_tracker.dto.ApplicationResponseDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.dto.VacancyResponseDto;
import com.job_tracker.entity.ActivityEventEntity;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.entity.VacancyEntity;
import com.job_tracker.enums.*;
import com.job_tracker.mapper.ActivityMapper;
import com.job_tracker.repository.ActivityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceImplTest {

    @Mock ActivityRepository repository;
    @Mock ActivityMapper mapper;
    @InjectMocks ActivityServiceImpl service;

    @Test
    void createActivitySuccessfully(){
        ActivityEventEntity activityEventEntity1 =  createActivityEventEntity();
        ActivityEventEntity activityEventEntity2 =  createActivityEventEntity();
        ActivityEventResponseDto activityEventResponseDto1 = createActivityEventResponseDto();
        ActivityEventResponseDto activityEventResponseDto2 = createActivityEventResponseDto();

        Page<ActivityEventEntity> page = new PageImpl<>(List.of(activityEventEntity1,activityEventEntity2));
        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(any(Pageable.class))).thenReturn(page);
        when(mapper.activityToDto(activityEventEntity1)).thenReturn(activityEventResponseDto1);
        when(mapper.activityToDto(activityEventEntity2)).thenReturn(activityEventResponseDto2);

        service.getAllActivityEvent(pageable);

        verify(repository).findAll(any(Pageable.class));
    }

    private ActivityEventResponseDto createActivityEventResponseDto(){
        return new ActivityEventResponseDto(
                1L,
                createApplicationResponseDto(),
                OffsetDateTime.now()
        );
    }

    private UserResponseDto createUserResponseDto(){
        return new UserResponseDto(
                createUserEntity().getName(),
                createUserEntity().getEmail(),
                createUserEntity().getRole()
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

    private ApplicationResponseDto createApplicationResponseDto(){
        return new ApplicationResponseDto(
                1L,
                createUserResponseDto(),
                createVacancyResponseDto(),
                ApplicationStatus.DRAFT,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                1L
        );
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

    private UserEntity createUserEntity(){
        return new UserEntity(
                1L,
                "Jahn",
                "jahn@gmail.com",
                "SecretPassword",
                Role.USER,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }
}
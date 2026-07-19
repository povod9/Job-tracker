package com.job_tracker.support;

import com.job_tracker.dto.*;
import com.job_tracker.entity.*;
import com.job_tracker.enums.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class ObjectMotherCreator {


    public UserEntity createUserEntity() {
        return new UserEntity(
                1L,
                "Jahn",
                "jahn@gmail.com",
                "SecretPassword",
                Role.USER,
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    public UserEntity createAdminEntity() {
        return new UserEntity(
                1L,
                "Jahn",
                "jahn@gmail.com",
                "SecretPassword",
                Role.ADMIN,
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    public UserEntity createHREntity() {
        return new UserEntity(
                1L,
                "Jahn",
                "jahn@gmail.com",
                "SecretPassword",
                Role.HR,
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    public UserResponseDto createUserResponseDto() {
        return new UserResponseDto("Jahn", "jahn@gmail.com", Role.USER);
    }

    public UserResponseDto createAdminResponseDto() {
        return new UserResponseDto("Jahn", "jahn@gmail.com", Role.ADMIN);
    }

    public UserResponseDto createHRResponseDto() {
        return new UserResponseDto("Jahn", "jahn@gmail.com", Role.HR);
    }

    public UserCreateRequestDto createUserRequestDto(){
        return new UserCreateRequestDto("Jahn", "jahn@gmail.com", "SecretPassword");
    }

    public PrincipalDto createUserPrincipal(){
        return new PrincipalDto("jahn@gmail.com", "USER", 1L);
    }

    public PrincipalDto createAdminPrincipal(){
        return new PrincipalDto("jahn@gmail.com", "ADMIN", 1L);
    }

    public PrincipalDto createHRPrincipal(){
        return new PrincipalDto("jahn@gmail.com", "HR", 1L);
    }

    public UserUpdatePasswordRequestDto createUpdPassRequest(){
        return new UserUpdatePasswordRequestDto("SecretPassword", "123");
    }

    public UserUpdateDto createUserUpdateDto(){
        return new UserUpdateDto("Lisa", "lisa@user.com");
    }

    public RequestLoginDto createLoginRequest(){
        return new RequestLoginDto("jahn@gmail.com", "SecretPassword");
    }

    public LoginResponseDto createLoginResponse(){
        return new LoginResponseDto("token", "Bearer");
    }


    public VacancyUpdateDto createVacancyUpdateDto(){
        return new VacancyUpdateDto(
                "pBank",
                "Manager",
                "description"
        );
    }

    public VacancyResponseDto updatedVacancyEntityResponse(){
        return new VacancyResponseDto(
                1L,
                "pBank",
                "Manager",
                "description",
                VacancyStatus.ACTIVE,
                VacancySource.MANUAL,
                BigDecimal.valueOf(5000.00),
                BigDecimal.valueOf(2000.00),
                List.of("Krakow"),
                "URL"
        );
    }

    public VacancyEntity createVacancyEntityWithStatusActive(){
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

    public VacancyCreateRequestDto createVacancyRequestDto(){
        return new VacancyCreateRequestDto(
                "Nike",
                "Cashier",
                "description",
                BigDecimal.valueOf(5000.00),
                BigDecimal.valueOf(2000.00),
                List.of("Krakow")
        );
    }



    public VacancyResponseDto createVacancyResponseDtoWithStatusActive(){
        return new VacancyResponseDto(
                1L,
                "Nike",
                "Cashier",
                "description",
                VacancyStatus.ACTIVE,
                VacancySource.MANUAL,
                BigDecimal.valueOf(5000.00),
                BigDecimal.valueOf(2000.00),
                List.of("Krakow"),
                "URL"
        );
    }

    public VacancyResponseDto createVacancyResponseDtoWithStatusDeleted(){
        return new VacancyResponseDto(
                1L,
                "Nike",
                "Cashier",
                "description",
                VacancyStatus.ACTIVE,
                VacancySource.MANUAL,
                BigDecimal.valueOf(5000.00),
                BigDecimal.valueOf(2000.00),
                List.of("Krakow"),
                "URL"
        );
    }

    public VacancyEntity createVacancyEntityWithStatusDeleted(){
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
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                1L,
                BigDecimal.valueOf(5000.00),
                BigDecimal.valueOf(2000.00),
                "URL"
        );
    }

    public ApplicationResponseDto createApplicationResponse(){
        return new ApplicationResponseDto(
                1L,
                createUserResponseDto(),
                createVacancyResponseDtoWithStatusActive(),
                ApplicationStatus.DRAFT,
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                1L
        );
    }

    public ApplicationCreateRequestDto createApplicationCreateRequest() {
        return new ApplicationCreateRequestDto(1L);
    }

    public ApplicationEntity createApplicationEntity(){
        return new ApplicationEntity(
                1L,
                createUserEntity(),
                createVacancyEntityWithStatusActive(),
                ApplicationStatus.DRAFT,
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                1L
        );
    }

    public ActivityEventEntity createActivityEventEntity(){
        return new ActivityEventEntity(
                1L,
                createApplicationEntity(),
                ActivityEventType.STATUS_CHANGED,
                OffsetDateTime.parse("2024-01-01T00:00:00Z"),
                createUserEntity()
        );
    }


    public ActivityEventResponseDto createActivityEventResponseDto(){
        return new ActivityEventResponseDto(
                1L,
                createApplicationResponse(),
                OffsetDateTime.parse("2024-01-01T00:00:00Z")
        );
    }

    public ReminderEntity createReminder(){
        return new ReminderEntity(
                1L,
                createUserEntity(),
                createApplicationEntity(),
                OffsetDateTime.parse("2027-01-01T00:00:00Z"),
                ReminderStatus.PENDING,
                "message",
                OffsetDateTime.parse("2027-01-01T00:00:00Z"),
                OffsetDateTime.parse("2027-01-01T00:00:00Z")
        );
    }

    public ReminderResponseDto createReminderResponse(){
        return new ReminderResponseDto(
                1L,
                createReminderApplicationResponse(),
                OffsetDateTime.parse("2027-01-01T00:00:00Z"),
                ReminderStatus.SENT,
                "message",
                OffsetDateTime.parse("2027-01-01T00:00:00Z"),
                OffsetDateTime.parse("2027-01-01T00:00:00Z")
        );
    }

    public ReminderApplicationResponseDto createReminderApplicationResponse(){
        return new ReminderApplicationResponseDto(
                1L,
                "Nike",
                "Cashier"
        );
    }

    public ReminderCreateRequestDto createReminderRequest(){
        return new ReminderCreateRequestDto(
                OffsetDateTime.parse("2027-01-01T00:00:00Z"),
                "message"
        );
    }

}

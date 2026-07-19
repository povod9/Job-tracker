package com.job_tracker.service.impl;

import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.dto.ReminderCreateRequestDto;
import com.job_tracker.dto.ReminderResponseDto;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.ReminderEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.mapper.ReminderMapper;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.ReminderRepository;
import com.job_tracker.repository.UserRepository;
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
class ReminderServiceImplTest {

    @Mock ReminderRepository reminderRepository;
    @Mock UserRepository userRepository;
    @Mock ApplicationRepository applicationRepository;
    @Mock ReminderMapper reminderMapper;
    @Mock SecurityContextService securityContextService;
    @InjectMocks ReminderServiceImpl service;
    ObjectMotherCreator objectMotherCreator = new ObjectMotherCreator();

    @Test
    void createReminderSuccessfully(){
        UserEntity userEntity = objectMotherCreator.createUserEntity();
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        ApplicationEntity applicationEntity = objectMotherCreator.createApplicationEntity();
        ReminderEntity reminderEntity = objectMotherCreator.createReminder();
        ReminderResponseDto expectedReminder = objectMotherCreator.createReminderResponse();
        ReminderCreateRequestDto reminderCreateRequest = objectMotherCreator.createReminderRequest();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        doNothing().when(securityContextService).validateOwnershipOrThrow(principalDto.id());
        when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(applicationEntity));
        when(reminderRepository.save(any(ReminderEntity.class))).thenReturn(reminderEntity);
        when(reminderMapper.reminderToDto(reminderEntity)).thenReturn(expectedReminder);

        var actual = service.createReminder(1L,reminderCreateRequest);

        ArgumentCaptor<ReminderEntity> captor = ArgumentCaptor.forClass(ReminderEntity.class);
        verify(reminderRepository).save(captor.capture());
        ReminderEntity reminderValue = captor.getValue();

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(securityContextService).validateOwnershipOrThrow(principalDto.id());
        verify(userRepository).findById(anyLong());
        verify(applicationRepository).findById(anyLong());
        verify(reminderRepository).save(any(ReminderEntity.class));
        verify(reminderMapper).reminderToDto(any(ReminderEntity.class));

        assertNotNull(actual);
        assertSame(expectedReminder,actual);
        assertEquals(reminderEntity.getUser().getName(), reminderValue.getUser().getName());
        assertEquals(reminderEntity.getUser().getEmail(), reminderValue.getUser().getEmail());
        assertEquals(reminderEntity.getUser().getRole(), reminderValue.getUser().getRole());
        assertEquals(reminderEntity.getMessage(), reminderValue.getMessage());
        assertEquals(reminderEntity.getStatus(), reminderValue.getStatus());
    }

    @Test
    void doNotCreateReminderIfUserDoesNotExists(){
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        ReminderCreateRequestDto reminderCreateRequest = objectMotherCreator.createReminderRequest();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        doNothing().when(securityContextService).validateOwnershipOrThrow(principalDto.id());
        when(userRepository.findById(principalDto.id())).thenReturn(Optional.empty());

        var actual = assertThrows(EntityNotFoundException.class,
                () -> service.createReminder(1L,reminderCreateRequest));

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(securityContextService).validateOwnershipOrThrow(principalDto.id());
        verify(userRepository).findById(anyLong());

        verify(applicationRepository, never()).findById(anyLong());
        verify(reminderRepository, never()).save(any(ReminderEntity.class));
        verify(reminderMapper, never()).reminderToDto(any(ReminderEntity.class));

        assertNotNull(actual);
        assertEquals("Cannot find user by id= " + principalDto.id(), actual.getMessage());
    }

    @Test
    void doNotCreateReminderIfApplicationDoesNotExists(){
        UserEntity userEntity = objectMotherCreator.createUserEntity();
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        ReminderCreateRequestDto reminderCreateRequest = objectMotherCreator.createReminderRequest();

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        doNothing().when(securityContextService).validateOwnershipOrThrow(principalDto.id());
        when(userRepository.findById(principalDto.id())).thenReturn(Optional.of(userEntity));
        when(applicationRepository.findById(1L)).thenReturn(Optional.empty());

        var actual = assertThrows(EntityNotFoundException.class,
                () -> service.createReminder(1L,reminderCreateRequest));

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(securityContextService).validateOwnershipOrThrow(principalDto.id());
        verify(userRepository).findById(anyLong());
        verify(applicationRepository).findById(anyLong());

        verify(reminderRepository, never()).save(any(ReminderEntity.class));
        verify(reminderMapper, never()).reminderToDto(any(ReminderEntity.class));

        assertNotNull(actual);
        assertEquals("Cannot find application by id= " + 1L, actual.getMessage());
    }

    @Test
    void listAllRemindersSuccessfully(){
        PrincipalDto principalDto = objectMotherCreator.createUserPrincipal();
        ReminderEntity reminderEntity1 = objectMotherCreator.createReminder();
        ReminderEntity reminderEntity2 = objectMotherCreator.createReminder();
        ReminderResponseDto reminderResponse1 = objectMotherCreator.createReminderResponse();
        ReminderResponseDto reminderResponse2 = objectMotherCreator.createReminderResponse();
        Pageable pageable = PageRequest.of(0,10);
        Page<ReminderEntity> page = new PageImpl<>(List.of(reminderEntity1, reminderEntity2));
        Page<ReminderResponseDto> expectedResponse = new PageImpl<>(List.of(reminderResponse1, reminderResponse2));

        when(securityContextService.getCurrentPrincipalOrThrow()).thenReturn(principalDto);
        when(reminderRepository.findAllByUserId(principalDto.id(), pageable)).thenReturn(page);
        when(reminderMapper.reminderToDto(reminderEntity1)).thenReturn(reminderResponse1);
        when(reminderMapper.reminderToDto(reminderEntity2)).thenReturn(reminderResponse2);

        var actual = service.getMyReminder(pageable);

        verify(securityContextService).getCurrentPrincipalOrThrow();
        verify(reminderRepository).findAllByUserId(anyLong(), any(Pageable.class));
        verify(reminderMapper, times(2)).reminderToDto(any(ReminderEntity.class));

        assertNotNull(actual);
        assertEquals(expectedResponse.getContent(), actual.getContent());
        assertEquals(expectedResponse.getTotalPages(), actual.getTotalPages());
        assertEquals(expectedResponse.getTotalElements(), actual.getTotalElements());
    }
}
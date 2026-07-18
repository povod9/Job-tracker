package com.job_tracker.service.impl;

import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.support.ObjectMotherCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HRServiceImplTest {

    @Mock UserRepository repository;
    @Mock UserMapper mapper;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks HRServiceImpl service;
    ObjectMotherCreator objectMotherCreator = new ObjectMotherCreator();

    @Test
    void createHrSuccessfully(){
        UserEntity userEntity = objectMotherCreator.createHREntity();
        UserResponseDto expectedResponse = objectMotherCreator.createHRResponseDto();
        UserCreateRequestDto userRequest = objectMotherCreator.createUserRequestDto();

        when(repository.existsByEmail(userRequest.email())).thenReturn(false);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(mapper.userToDto(userEntity)).thenReturn(expectedResponse);
        when(passwordEncoder.encode(userRequest.password())).thenReturn("HashedPassword");

        var actual = service.createHR(userRequest);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(repository).save(captor.capture());
        UserEntity userValue = captor.getValue();

        verify(repository).existsByEmail(anyString());
        verify(mapper).userToDto(any(UserEntity.class));
        verify(passwordEncoder).encode(anyString());

        assertNotNull(actual);
        assertSame(expectedResponse, actual);
        assertEquals(userEntity.getRole(), userValue.getRole());
        assertEquals(userEntity.getName(), userValue.getName());
        assertEquals(userEntity.getEmail(), userValue.getEmail());
        assertEquals("HashedPassword", userValue.getPassword());
    }

    @Test
    void doNotCreateHrIfEmailExists(){
        UserCreateRequestDto userRequest = objectMotherCreator.createUserRequestDto();

        when(repository.existsByEmail(userRequest.email())).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.createHR(userRequest));

        verify(repository, never()).save(any(UserEntity.class));
        verify(mapper, never()).userToDto(any(UserEntity.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
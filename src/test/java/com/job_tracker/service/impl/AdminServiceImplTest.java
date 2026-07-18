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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock UserRepository repository;
    @Mock UserMapper mapper;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks AdminServiceImpl adminService;
    ObjectMotherCreator objectMotherCreator = new ObjectMotherCreator();

    @Test
    void createAdminSuccessfully(){
        UserCreateRequestDto userCreateRequestDto = objectMotherCreator.createUserRequestDto();
        UserEntity userEntity = objectMotherCreator.createAdminEntity();
        UserResponseDto userResponseDto = objectMotherCreator.createAdminResponseDto();

        when(repository.existsByEmail(userCreateRequestDto.email())).thenReturn(false);
        when(repository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(passwordEncoder.encode(userCreateRequestDto.password())).thenReturn("HashPassword");
        when(mapper.userToDto(userEntity)).thenReturn(userResponseDto);


        var actual = adminService.createAdmin(userCreateRequestDto);
        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        verify(repository).save(argumentCaptor.capture());
        verify(passwordEncoder).encode(anyString());
        verify(mapper).userToDto(any(UserEntity.class));
        UserEntity captured = argumentCaptor.getValue();

        assertEquals(captured.getName(), actual.name());
        assertEquals(captured.getEmail(), actual.email());
        assertEquals(captured.getRole(), actual.role());
    }

    @Test
    void doNotCreateAdminIfEmailExists(){
        UserCreateRequestDto userCreateRequestDto = objectMotherCreator.createUserRequestDto();

        when(repository.existsByEmail(userCreateRequestDto.email())).thenReturn(true);

        var actualErrorMessage = assertThrows(IllegalArgumentException.class,
                () -> adminService.createAdmin(userCreateRequestDto));

        verify(repository, never()).save(any(UserEntity.class));
        verify(mapper, never()).userToDto(any(UserEntity.class));
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals("Email already exists " + userCreateRequestDto.email(), actualErrorMessage.getMessage());
    }
}
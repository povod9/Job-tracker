package com.job_tracker.service.impl;

import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.OffsetDateTime;
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

    @Test
    void createAdminSuccessfully(){
        UserCreateRequestDto userCreateRequestDto = createUserRequestDto();
        UserEntity userEntity = createUserEntity();
        UserResponseDto userResponseDto = createUserResponseDto();

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
        UserCreateRequestDto userCreateRequestDto = createUserRequestDto();

        when(repository.existsByEmail(userCreateRequestDto.email())).thenReturn(true);

        var actualErrorMessage = assertThrows(IllegalArgumentException.class,
                () -> adminService.createAdmin(userCreateRequestDto));

        verify(repository, never()).save(any(UserEntity.class));
        verify(mapper, never()).userToDto(any(UserEntity.class));
        verify(passwordEncoder, never()).encode(anyString());
        assertEquals("Email already exists " + userCreateRequestDto.email(), actualErrorMessage.getMessage());
    }

    private UserCreateRequestDto createUserRequestDto(){
        return new UserCreateRequestDto("Jahn", "jahn@gmail.com", "SecretPassword");
    }

    private UserEntity createUserEntity() {
        return new UserEntity(
                1L,
                "Jahn",
                "jahn@gmail.com",
                "SecretPassword",
                Role.ADMIN,
                OffsetDateTime.now(),
                OffsetDateTime.now());
    }

    private UserResponseDto createUserResponseDto() {
        return new UserResponseDto("Jahn", "jahn@gmail.com", Role.ADMIN);
    }
}
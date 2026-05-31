package com.job_tracker.service.impl;

import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.dto.UserCreateRequestDto;
import com.job_tracker.dto.UserResponseDto;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.Role;
import com.job_tracker.mapper.UserMapper;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.security.PasswordHash;
import com.job_tracker.service.HRService;
import com.job_tracker.service.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HRServiceImpl implements HRService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final SecurityContextService securityContextService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    @Transactional
    public UserResponseDto createHR(UserCreateRequestDto userCreateRequestDto) {
        PrincipalDto principalDto = securityContextService.getCurrentPrincipalOrThrow();

        if (repository.existsByEmail(userCreateRequestDto.email())){
            throw new IllegalArgumentException("Email already exists: " + userCreateRequestDto.email());
        }

        UserEntity createdUserEntity = new UserEntity(
                null,
                userCreateRequestDto.name(),
                userCreateRequestDto.email(),
                passwordEncoder.encode(userCreateRequestDto.passwordHash()),
                Role.HR,
                null,
                null
        );
        UserEntity savedUser = repository.save(createdUserEntity);
        return mapper.userToDto(savedUser);
    }
}

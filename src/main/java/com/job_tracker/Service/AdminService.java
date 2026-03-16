package com.job_tracker.Service;

import com.job_tracker.Dto.UserCreateRequestDto;
import com.job_tracker.Dto.UserResponseDto;
import com.job_tracker.Entity.UserEntity;
import com.job_tracker.Enums.Role;
import com.job_tracker.Mapper.UserMapper;
import com.job_tracker.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }


    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto createAdmin(
            UserCreateRequestDto user
    ) {

        if(userRepository.existsByEmail(user.email())){
            throw new IllegalArgumentException("Email already exists" + user.email());
        }

        UserEntity userEntity = new UserEntity(
                null,
                user.name(),
                user.email(),
                passwordEncoder.encode(user.passwordHash()),
                Role.ADMIN,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
        userEntity = userRepository.save(userEntity);

        return mapper.userToDto(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto getUserByEmail(
            String email
    )
    {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by email " + email));

        return mapper.userToDto(userEntity);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDto deleteUser(
            String email
    )
    {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()  -> new EntityNotFoundException("Cannot find user by email " + email));

        userRepository.delete(userEntity);
        return mapper.userToDto(userEntity);
    }
}

package com.job_tracker.Service;

import com.job_tracker.CreateException.InvalidCredentialsException;
import com.job_tracker.Dto.*;
import com.job_tracker.Entity.UserEntity;
import com.job_tracker.Enums.Role;
import com.job_tracker.Mapper.UserMapper;
import com.job_tracker.Repository.*;
import com.job_tracker.Security.JwtCore;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtCore  jwtCore;

    public UserService(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder, JwtCore jwtCore) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtCore = jwtCore;
    }


    public UserResponseDto userToCreate(
            UserCreateRequestDto user
    )
    {
        if(userRepository.existsByEmail(user.email())){
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity userEntity = new UserEntity(
                null,
                user.name(),
                user.email(),
                passwordEncoder.encode(user.passwordHash()),
                Role.USER,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        userEntity = userRepository.save(userEntity);
        return mapper.userToDto(userEntity);
    }



    public LoginResponseDto userToLogin(
            RequestLoginDto user
    )
    {
        UserEntity userEntity = userRepository.findByEmail(user.email())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by email " + user.email()));


        if(passwordEncoder.matches(user.passwordHash(), userEntity.getPasswordHash())){
            LoginResponseDto loginResponseDto = new LoginResponseDto(jwtCore.generateJwtToken(userEntity),
                    "Bearer");
            return loginResponseDto;
        }else {
            throw new InvalidCredentialsException("Wrong password or email");
        }
    }


    public UserResponseDto userToUpdate(
            UserUpdateDto user
    )
    {
        if(userRepository.existsByEmail(user.email())){
            throw new IllegalArgumentException("Email already exists");
        }
    }
}

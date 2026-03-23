package com.job_tracker.Service;

import com.job_tracker.CreateException.AccessDeniedException;
import com.job_tracker.CreateException.InvalidCredentialsException;
import com.job_tracker.Dto.*;
import com.job_tracker.Entity.ActivityEventEntity;
import com.job_tracker.Entity.ApplicationEntity;
import com.job_tracker.Entity.UserEntity;
import com.job_tracker.Enums.ActivityEventType;
import com.job_tracker.Enums.ApplicationStatus;
import com.job_tracker.Enums.Role;
import com.job_tracker.Mapper.UserMapper;
import com.job_tracker.Repository.*;
import com.job_tracker.Security.JwtCore;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtCore  jwtCore;
    private final ActivityRepository activityRepository;

    public UserService(UserRepository userRepository, ApplicationRepository applicationRepository, UserMapper mapper, PasswordEncoder passwordEncoder, JwtCore jwtCore, ActivityRepository activityRepository) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtCore = jwtCore;
        this.activityRepository = activityRepository;
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


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserResponseDto userToUpdate(
            UserUpdateDto user
    )
    {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();

        UserEntity userEntity = userRepository.findById(principal.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

        if(!userEntity.getId().equals(principal.id())){
            throw new AccessDeniedException("Access denied");
        }

        if(user.name() != null){
            userEntity.setName(user.name());
        }

        if(user.email() != null){
            if(user.email().equals(userEntity.getEmail())){
                throw new IllegalArgumentException("Email cannot be the same");
            }

            if(userRepository.existsByEmail(user.email())){
                throw new IllegalArgumentException("Email already exists");
            }

            userEntity.setEmail(user.email());
        }

        if (user.passwordHash() != null) {
            if(passwordEncoder.matches(user.passwordHash(), userEntity.getPasswordHash())){
                throw new IllegalArgumentException("Password cannot be the same");
            }
            userEntity.setPasswordHash(passwordEncoder.encode(user.passwordHash()));
        }

        userEntity.setUpdatedAt(OffsetDateTime.now());
        userEntity = userRepository.save(userEntity);
        return mapper.userToDto(userEntity);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApplicationResponseDto createApplication(
            ApplicationCreateRequestDto application
    )
    {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();

     UserEntity userEntity = userRepository.findById(principal.id())
             .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

        ApplicationEntity applicationEntity = new ApplicationEntity(
                null,
                userEntity,
                application.company(),
                application.position(),
                ApplicationStatus.DRAFT,
                null,
                null,
                null
        );

        if(application.company().equals(applicationEntity.getCompany()) && application.position().equals(applicationEntity.getPosition())){
            throw new IllegalArgumentException("This application already exists");
        }

        ApplicationEntity saved = applicationRepository.save(applicationEntity);
        return mapper.applicationToDto(saved);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ApplicationResponseDto> getMyApplication() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();

        UserEntity userEntity = userRepository.findById(principal.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

        List<ApplicationEntity> userApplicationEntity = applicationRepository.findByUserIdAndApplicationStatusNot(
                principal.id(),
                ApplicationStatus.DELETED
        );
        return mapper.listApplicationToDto(userApplicationEntity);
    }


    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApplicationResponseDto deleteMyApplicationById(
            Long id
    )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();

        ApplicationEntity applicationEntity = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find application by id= " + id));

        UserEntity userEntity = userRepository.findById(principal.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + id));

        if(!applicationEntity.getUser().getId().equals(principal.id())){
            throw new AccessDeniedException("You are not allowed to delete this application");
        }

        applicationEntity.setApplicationStatus(ApplicationStatus.DELETED);

        ActivityEventEntity activityEventEntity = new ActivityEventEntity(
                null,
                applicationEntity,
                ActivityEventType.STATUS_CHANGED,
                null,
                userEntity
        );

        applicationRepository.save(applicationEntity);
        activityRepository.save(activityEventEntity);

        return mapper.applicationToDto(applicationEntity);
    }
}


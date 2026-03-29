package com.job_tracker.Service;

import com.job_tracker.CreateException.AccessDeniedException;
import com.job_tracker.CreateException.InvalidApplicationStatusTransition;
import com.job_tracker.Dto.ApplicationCreateRequestDto;
import com.job_tracker.Dto.ApplicationResponseDto;
import com.job_tracker.Dto.PrincipalDto;
import com.job_tracker.Entity.ActivityEventEntity;
import com.job_tracker.Entity.ApplicationEntity;
import com.job_tracker.Entity.UserEntity;
import com.job_tracker.Enums.ActivityEventType;
import com.job_tracker.Enums.ApplicationStatus;
import com.job_tracker.Mapper.ApplicationMapper;
import com.job_tracker.Mapper.UserMapper;
import com.job_tracker.Repository.ActivityRepository;
import com.job_tracker.Repository.ApplicationRepository;
import com.job_tracker.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicationService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ActivityRepository activityRepository;
    private final ApplicationMapper applicationMapper;

    public ApplicationService(UserRepository userRepository, ApplicationRepository applicationRepository, ActivityRepository activityRepository, ApplicationMapper applicationMapper) {
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.activityRepository = activityRepository;
        this.applicationMapper = applicationMapper;
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


        if(applicationRepository.existsByUserIdAndCompanyAndPosition(principal.id(), application.company(), application.position())){
            throw new IllegalArgumentException("Application already exists");
        }

        ApplicationEntity createdApplicationEntity = new ApplicationEntity(
                null,
                userEntity,
                application.company(),
                application.position(),
                ApplicationStatus.DRAFT,
                null,
                null,
                null
        );
        ApplicationEntity saved = applicationRepository.save(createdApplicationEntity);
        return applicationMapper.applicationToDto(saved);
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
        return applicationMapper.listApplicationToDto(userApplicationEntity);
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

        return applicationMapper.applicationToDto(applicationEntity);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ApplicationResponseDto updateMyApplicationStatusById(
            Long id,
            ApplicationStatus applicationStatus
    )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();

        UserEntity userEntity = userRepository.findById(principal.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + id));

        ApplicationEntity applicationEntity = applicationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find application by id= " + id));

        if(!applicationEntity.getApplicationStatus().canTransitionTo(applicationStatus)){
            throw new InvalidApplicationStatusTransition("Application status can't be transitioned to " + applicationStatus);
        }

        if(!applicationEntity.getUser().getId().equals(principal.id())){
            throw new AccessDeniedException("You are not allowed to update this application");
        }

        ActivityEventEntity activityEventEntity = new ActivityEventEntity(
                null,
                applicationEntity,
                ActivityEventType.STATUS_CHANGED,
                null,
                userEntity
        );

        applicationEntity.setApplicationStatus(applicationStatus);
        applicationRepository.save(applicationEntity);
        activityRepository.save(activityEventEntity);

        return applicationMapper.applicationToDto(applicationEntity);
    }
}

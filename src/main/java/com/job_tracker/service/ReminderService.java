package com.job_tracker.service;

import com.job_tracker.create_exception.AccessDeniedException;
import com.job_tracker.dto.PrincipalDto;
import com.job_tracker.dto.ReminderCreateRequestDto;
import com.job_tracker.dto.ReminderResponseDto;
import com.job_tracker.entity.ApplicationEntity;
import com.job_tracker.entity.ReminderEntity;
import com.job_tracker.entity.UserEntity;
import com.job_tracker.enums.ReminderStatus;
import com.job_tracker.mapper.ReminderMapper;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.ReminderRepository;
import com.job_tracker.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ReminderMapper  reminderMapper;

    public ReminderService(ReminderRepository reminderRepository, UserRepository userRepository, ApplicationRepository applicationRepository, ReminderMapper reminderMapper) {
        this.reminderRepository = reminderRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.reminderMapper = reminderMapper;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<ReminderResponseDto> getMyReminder()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();

        List<ReminderEntity> reminderEntities = reminderRepository.findAllByUserId(principal.id());

        return reminderMapper.listRemindersToDto(reminderEntities);
    }

    @Transactional
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ReminderResponseDto createReminder(
            Long applicationId,
            ReminderCreateRequestDto reminder
    )
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDto principal = (PrincipalDto) authentication.getPrincipal();

        UserEntity userEntity = userRepository.findById(principal.id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot find user by id= " + principal.id()));

        ApplicationEntity applicationEntity = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find application by id= " + applicationId));

        if(!applicationEntity.getUser().getId().equals(principal.id())){
            throw new AccessDeniedException("You are not allowed to create this reminder");
        }

        ReminderEntity reminderEntity = new ReminderEntity(
                null,
                userEntity,
                applicationEntity,
                reminder.dueAt(),
                ReminderStatus.PENDING,
                reminder.message(),
                null,
                null
        );

        reminderRepository.save(reminderEntity);
        return reminderMapper.reminderToDto(reminderEntity);
    }
}

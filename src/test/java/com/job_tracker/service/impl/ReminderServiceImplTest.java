package com.job_tracker.service.impl;

import com.job_tracker.mapper.ReminderMapper;
import com.job_tracker.repository.ApplicationRepository;
import com.job_tracker.repository.ReminderRepository;
import com.job_tracker.repository.UserRepository;
import com.job_tracker.service.SecurityContextService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReminderServiceImplTest {

    @Mock ReminderRepository reminderRepository;
    @Mock UserRepository userRepository;
    @Mock ApplicationRepository applicationRepository;
    @Mock ReminderMapper reminderMapper;
    @Mock SecurityContextService securityContextService;
    @InjectMocks ReminderServiceImpl service;

    @Test
    void createReminderSuccessfully(){

    }
}
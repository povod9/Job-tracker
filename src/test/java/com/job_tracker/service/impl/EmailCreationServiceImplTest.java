package com.job_tracker.service.impl;

import com.job_tracker.entity.EmailEntity;
import com.job_tracker.entity.ReminderEntity;
import com.job_tracker.enums.EmailStatus;
import com.job_tracker.enums.ReminderStatus;
import com.job_tracker.repository.EmailRepository;
import com.job_tracker.support.ObjectMotherCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailCreationServiceImplTest {

    @Mock EmailRepository emailRepository;
    @InjectMocks EmailCreationServiceImpl service;
    ObjectMotherCreator objectMotherCreator = new ObjectMotherCreator();

    @Test
    void createEmailForReminderSuccessfully(){
        EmailEntity emailEntity = objectMotherCreator.createEmailEntity();
        ReminderEntity reminderEntity = objectMotherCreator.createReminder();

        when(emailRepository.save(any(EmailEntity.class))).thenReturn(emailEntity);

        service.createEmailForReminder(reminderEntity);

        ArgumentCaptor<EmailEntity> captor = ArgumentCaptor.forClass(EmailEntity.class);
        verify(emailRepository).save(captor.capture());
        EmailEntity emailValue = captor.getValue();

        assertEquals(EmailStatus.PENDING, emailValue.getStatus());
        assertEquals(emailEntity.getUser().getEmail(), emailValue.getUser().getEmail());
        assertEquals(emailEntity.getUser().getName(), emailValue.getUser().getName());
        assertEquals(ReminderStatus.PENDING, emailValue.getReminder().getStatus());
        assertEquals(emailEntity.getReminder().getDueAt(), emailValue.getReminder().getDueAt());
        assertEquals(emailEntity.getReminder().getMessage(), emailValue.getReminder().getMessage());
    }

    @Test
    void doNotThrowException(){
        ReminderEntity reminderEntity = objectMotherCreator.createReminder();

        when(emailRepository.save(any(EmailEntity.class))).thenThrow(new RuntimeException("Failed to create email for reminder {}: {}"));

        assertDoesNotThrow(() -> service.createEmailForReminder(reminderEntity));

        verify(emailRepository).save(any(EmailEntity.class));
    }
}
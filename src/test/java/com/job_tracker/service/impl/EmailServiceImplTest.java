package com.job_tracker.service.impl;

import com.job_tracker.entity.EmailEntity;
import com.job_tracker.enums.EmailStatus;
import com.job_tracker.repository.EmailRepository;
import com.job_tracker.support.ObjectMotherCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock JavaMailSender mailSender;
    @Mock EmailRepository repository;
    @InjectMocks EmailServiceImpl service;
    ObjectMotherCreator objectMotherCreator = new ObjectMotherCreator();

    @Test
    void sendEmailSuccessfully(){
        EmailEntity emailEntity = objectMotherCreator.createEmailEntity();

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        when(repository.save(any(EmailEntity.class))).thenReturn(emailEntity);

        service.sendEmail(emailEntity);
        verify(repository).save(any(EmailEntity.class));

        assertEquals(0, emailEntity.getAttempts());
        assertEquals(EmailStatus.SENT, emailEntity.getStatus());
        assertNull(emailEntity.getErrorMessage());
    }

    @Test
    void increaseAttemptIfException(){
        EmailEntity emailEntity = objectMotherCreator.createEmailEntity();

        doThrow(RuntimeException.class).when(mailSender).send(any(SimpleMailMessage.class));

        when(repository.save(any(EmailEntity.class))).thenReturn(emailEntity);

        service.sendEmail(emailEntity);

        verify(repository).save(any(EmailEntity.class));

        assertEquals(1,emailEntity.getAttempts());
        assertEquals(new Exception().getMessage(), emailEntity.getErrorMessage());
        assertEquals(EmailStatus.PENDING, emailEntity.getStatus());
    }

    @Test
    void failedSendingIfAttemptsMoreThanFive(){
        EmailEntity emailEntity = objectMotherCreator.createEmailEntity();
        emailEntity.setAttempts(5);

        doThrow(new RuntimeException()).when(mailSender).send(any(SimpleMailMessage.class));

        when(repository.save(any(EmailEntity.class))).thenReturn(emailEntity);

        service.sendEmail(emailEntity);

        verify(repository).save(any(EmailEntity.class));

        assertEquals(6, emailEntity.getAttempts());
        assertEquals(EmailStatus.FAILED, emailEntity.getStatus());
        assertEquals(new Exception().getMessage(), emailEntity.getErrorMessage());
    }
}
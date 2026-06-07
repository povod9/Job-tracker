package com.job_tracker.service;

import com.job_tracker.entity.ReminderEntity;

public interface EmailCreationService {

    void createEmailForReminder(ReminderEntity reminder);
}

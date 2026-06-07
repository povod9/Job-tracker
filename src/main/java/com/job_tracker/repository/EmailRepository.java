package com.job_tracker.repository;

import com.job_tracker.entity.EmailEntity;
import com.job_tracker.enums.EmailStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, Long> {

    @EntityGraph(attributePaths = {"user", "reminder", "reminder.application", "reminder.application.vacancy"})
    List<EmailEntity> findEmailByStatus(EmailStatus status);

}
